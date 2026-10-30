
chrome.runtime.onMessage.addListener((message, sender, sendResponse) => {

    if (message.action === "closeTab") {
        if (sender.tab?.id != null) {
            chrome.tabs.remove(sender.tab.id);
        }
        return;
    }

    if (message.action !== "API_REQUEST") return;

    const allowedPaths = [
        "/session/status",
        "/content/check",
        "/session/add/distraction"
    ];

    if (!allowedPaths.includes(message.path)) {
        sendResponse({ ok: false, error: "Invalid API path" });
        return;
    }

    const url = "https://lockedin-6k2g.onrender.com" + message.path;

    fetch(url, {
        method: message.method || "GET",
        headers: {
            ...(message.body !== undefined
                ? { "Content-Type": "application/json" }
                : {}),
            ...(message.token
                ? { "Authorization": "Bearer " + message.token }
                : {})
        },
        ...(message.body !== undefined
            ? { body: JSON.stringify(message.body) }
            : {})
    })
        .then(async response => {
            const text = await response.text();

            sendResponse({
                ok: response.ok,
                status: response.status,
                data: text
            });
        })
        .catch(error => {
            sendResponse({
                ok: false,
                error: error.message
            });
        });

    return true;
});
