let lastUrl = location.href;
let checkTimeout = null;
console.log("Chrome object =", chrome);
console.log("Chrome storage =", chrome.storage);

scheduleCheck();

new MutationObserver(() => {

    if (location.href !== lastUrl) {
        lastUrl = location.href;
        console.log("URL Changed:", lastUrl);
        scheduleCheck();
    }

}).observe(document, {
    childList: true,
    subtree: true
});

function scheduleCheck() {

    if (checkTimeout) {
        clearTimeout(checkTimeout);
    }
    checkTimeout = setTimeout(() => {initializeLockedIn();}, 10000); // wait 10 seconds
}

async function initializeLockedIn() {
    console.log("=================================");
    console.log("LOCKEDIN INITIALIZED");
    const sessionRunning = await checkFocusSession();
    console.log("SESSION STATUS:", sessionRunning);
    if (!sessionRunning) {
        console.log("Session not active");
        removePopup();
        return;
    }

    const pageData = collectPageData();
    if (!pageData) {
        removePopup();
        console.log("No page data collected");
        return;
    }

    const currentUrl = location.href;

    console.log("PAGE DATA:", pageData);
    console.log("CONTENT =", pageData.content);

    let response;
    try {
        response = await classifyContent(pageData);
    } catch (error) {
        console.error("Classification Error:", error);
        return;
    }

// User changed page while AI was working
    if (currentUrl !== location.href) {
        console.log("Page changed. Ignoring old response.");
        return;
    }

    console.log("CLASSIFICATION RESPONSE:", response);

    handleClassification(response);
}


async function checkFocusSession() {
    try {
        const result = await chrome.storage.local.get(["token"]);

        if (!result.token) {
            console.log("No JWT found");
            return null;
        }

        const response = await chrome.runtime.sendMessage({
            action: "API_REQUEST",
            path: "/session/status",
            method: "GET",
            token: result.token
        });

        if (!response?.ok) {
            console.log("SESSION API FAILURE DETAILS:", JSON.stringify(response, null, 2));
            console.log("Token exists:", !!result.token);
            return null;
        }

        const sessionState = String(response.data).trim();

        console.log("Backend Session State:", sessionState);

        return sessionState === "ACTIVE";

    } catch (error) {
        console.error("Session communication failed:", error);
        return null;
    }
}


function collectPageData() {
    const hostname = window.location.hostname;
    const pathname = window.location.pathname;
    console.log("HOST:", hostname);
    console.log("PATH:", pathname);
    console.log("TITLE:", document.title);

    if (hostname.includes("youtube.com")) {
        const isWatch = pathname === "/watch";
        const isShorts = pathname.startsWith("/shorts");
        if (!isWatch && !isShorts) {
            console.log("Not a YouTube video or Shorts page.");
            return null;
        }
    }

    let title = document.title;

    if (hostname.includes("youtube.com")) {

        const titleElement =
            document.querySelector("h1 yt-formatted-string") ||
            document.querySelector("h1");

        if (titleElement && titleElement.textContent.trim()) {
            title = titleElement.textContent.trim();
        }

        console.log("CONTENT =", title);
    }

    return {
        content: title,
        source: hostname
    };
}


async function classifyContent(pageData) {
    const result = await chrome.storage.local.get(["token"]);

    const response = await chrome.runtime.sendMessage({
        action: "API_REQUEST",
        path: "/content/check",
        method: "POST",
        token: result.token,
        body: pageData
    });

    if (!response?.ok) {
        throw new Error(
            response?.error || `API failed: ${response?.status}`
        );
    }

    return typeof response.data === "string"
        ? JSON.parse(response.data)
        : response.data;
}


async function handleClassification(response) {
    console.log("Allowed:", response.allowed);

    if (response.allowed) {
        removePopup();
        return;
    }

    try {
        await addDistraction();
    } catch (error) {
        console.error("Distraction recording failed:", error);
    }

    await injectRoastPopup(response);
}


async function addDistraction() {
    const result = await chrome.storage.local.get(["token"]);

    const response = await chrome.runtime.sendMessage({
        action: "API_REQUEST",
        path: "/session/add/distraction",
        method: "POST",
        token: result.token,
        body: 0
    });

    if (!response?.ok) {
        console.error("Failed to add distraction:", response);
    } else {
        console.log("Distraction recorded successfully");
    }
}


async function injectRoastPopup(response) {
    if (document.getElementById("lockedIn-overlay")) {
        console.log("Popup already exists.");
        return;
    }
    console.log("Injecting popup...");

    try {
        const popupHtml = await fetch(chrome.runtime.getURL("roastPopUp.html"));
        const html = await popupHtml.text();
        document.body.insertAdjacentHTML("beforeend", html);

        const roastMessage = document.getElementById("roastMessage");
        if (roastMessage) {
            roastMessage.textContent = response.roast || "Get back to work.";
        }

        const button = document.getElementById("backToStudyBtn");

        if (button) {
            button.addEventListener("click", handleBackToStudy);
        }

        console.log("Popup injected successfully.");

    } catch (error) {
        console.error("Popup Injection Failed:", error);
    }
}

function removePopup() {
    document.getElementById("lockedIn-overlay")?.remove();
}

function handleBackToStudy() {
    removePopup();
    const hostname = window.location.hostname;
    if (hostname.includes("youtube.com")) {
        window.location.href = "https://www.youtube.com";
        return;
    }
    chrome.runtime.sendMessage({action: "closeTab"});
}