// ================= AUTH SECTIONS =================
const statusDot = document.getElementById("statusDot");
const loginSection = document.getElementById("loginSection");
const registerSection = document.getElementById("registerSection");
const dashboardSection = document.getElementById("dashboardSection");

// ================= LOGIN =================

const loginEmail = document.getElementById("loginEmail");
const loginPassword = document.getElementById("loginPassword");
const loginBtn = document.getElementById("loginBtn");
const showRegisterLink = document.getElementById("showRegister");

// ================= REGISTER =================

const registerName = document.getElementById("registerName");
const registerEmail = document.getElementById("registerEmail");
const registerParentEmail = document.getElementById("registerParentEmail");
const registerPassword = document.getElementById("registerPassword");
const registerBtn = document.getElementById("registerBtn");
const showLoginLink = document.getElementById("showLogin");

// ================= DASHBOARD =================

const welcomeUser = document.getElementById("welcomeUser");
const logoutBtn = document.getElementById("logoutBtn");

//status
const status = document.getElementById("status");

//buttons
const startBtn = document.getElementById("startBtn");
const pauseBtn = document.getElementById("pauseBtn");
const resumeBtn = document.getElementById("resumeBtn");
const endBtn = document.getElementById("endBtn");
const pausedEndBtn = document.getElementById("pausedEndBtn");

//layouts
const startLayout = document.getElementById("startLayout");
const activeLayout = document.getElementById("activeLayout");
const pausedLayout = document.getElementById("pausedLayout");

//task
const taskInput = document.getElementById("taskInput");
const addTaskBtn = document.getElementById("addTaskBtn");
const taskList = document.getElementById("taskList");

function showLogin() {
    loginSection.hidden = false;
    registerSection.hidden = true;
    dashboardSection.hidden = true;
}

function showRegister() {
    loginSection.hidden = true;
    registerSection.hidden = false;
    dashboardSection.hidden = true;
}

function showDashboard(name) {
    loginSection.hidden = true;
    registerSection.hidden = true;
    dashboardSection.hidden = false;
    welcomeUser.innerText = `Welcome, ${name} 👋`;
}

async function getAuthHeaders() {
    const result = await chrome.storage.local.get(["token"]);
    return {
        "Content-Type": "application/json",
        "Authorization": "Bearer " + result.token
    };
}

async function login() {
    const email = loginEmail.value;
    const password = loginPassword.value;

    try {
        const response = await fetch("http://localhost:8080/auth/login", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({email: email, password: password})
        });

        if (!response.ok) {
            alert("Invalid Email or Password");
            return;
        }

        const data = await response.json();
        await chrome.storage.local.set({token: data.token, name: data.name});

        try {
            await fetch("http://localhost:9090/token", {
                method: "POST",
                headers: {"Content-Type": "text/plain"},
                body: data.token
            });
        } catch (error) {
            console.error("Failed to send JWT to Desktop Companion:", error);
        }

        showDashboard(data.name);
        await loadSessionStatus();
        await loadTask();

    } catch (error) {
        console.error("Login Error:", error);
        alert("Unable to connect to the backend.");
    }
}

async function register() {
    const name = registerName.value;
    const email = registerEmail.value;
    const parentEmail = registerParentEmail.value;
    const password = registerPassword.value;
    const response = await fetch("http://localhost:8080/auth/register", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({name: name, email: email, password: password, parentEmail: parentEmail})
    });
    if (!response.ok) {
        alert("Registration Failed");
        return;
    }
    const data = await response.json();
    await chrome.storage.local.set({token: data.token, name: data.name});
    showDashboard(data.name);
    await loadSessionStatus();
    await loadTask();
}

async function logout() {
    await fetch("http://localhost:8080/auth/logOut", {
        method: "POST",
        headers: await getAuthHeaders()
    });

    await chrome.storage.local.remove(["token", "name"]);

    showLogin();
    loginEmail.value = "";
    loginPassword.value = "";
    registerName.value = "";
    registerEmail.value = "";
    registerParentEmail.value = "";
    registerPassword.value = "";
}

async function loadSessionStatus(){
    const response = await fetch("http://localhost:8080/session/status", {method: "GET" , headers: await getAuthHeaders()});
    const state = await response.text();
    updateSessionUI(state);
}

function updateSessionUI(state){

    if(state === "ACTIVE"){

        status.innerText = "Active";
        statusDot.style.backgroundColor = "#9EF084";   // Green

        startLayout.hidden = true;
        activeLayout.hidden = false;
        pausedLayout.hidden = true;
    }

    else if(state === "PAUSED"){

        status.innerText = "Paused";
        statusDot.style.backgroundColor = "#F3E983";   // Yellow

        startLayout.hidden = true;
        activeLayout.hidden = true;
        pausedLayout.hidden = false;
    }

    else{

        status.innerText = "Not Started";
        statusDot.style.backgroundColor = "#FFFFFF";   // White

        startLayout.hidden = false;
        activeLayout.hidden = true;
        pausedLayout.hidden = true;
    }
}

async function startSession(){
    const response = await fetch("http://localhost:8080/session/start", {method: "POST" , headers: await getAuthHeaders()});
    if (!response.ok) {
        alert("Failed to start session");
        return;
    }
    updateSessionUI("ACTIVE");
}

async function pauseSession(){
    const response = await fetch("http://localhost:8080/session/pause", {method: "POST" , headers: await getAuthHeaders()});
    if (!response.ok) {
        alert("Failed to pause session");
        return;
    }
    updateSessionUI("PAUSED");
}

async function resumeSession(){
    const response = await fetch("http://localhost:8080/session/resume", {method: "POST" , headers: await getAuthHeaders()});
    if (!response.ok) {
        alert("Failed to resume session");
        return;
    }
    updateSessionUI("ACTIVE");
}

async function endSession(){
    const response = await fetch("http://localhost:8080/session/end", {method: "POST" , headers: await getAuthHeaders()});
    if (!response.ok) {
        alert("Failed to end session");
        return;
    }
    updateSessionUI("ENDED");
}

async function loadTask(){
    const response = await fetch("http://localhost:8080/task/remaining", {method: "GET" , headers: await getAuthHeaders()});
    if (!response.ok) {
        alert("Failed to load tasks");
        return;
    }
    const tasks = await response.json();
    displayTask(tasks);
}

async function addTask() {
    const title = taskInput.value.trim();
    if (title === "") {
        return;
    }
    await fetch("http://localhost:8080/task/add", {
        method: "POST",
        headers: await getAuthHeaders(),
        body: JSON.stringify({
            title: title
        })
    });
    taskInput.value = "";
    await loadTask();
}

async function completeTask(id){
    console.log("Completing task:", id);
    await fetch(`http://localhost:8080/task/complete/${id}`, {method: "PUT" ,  headers: await getAuthHeaders()});
    await loadTask();
}

async function deleteTask(id){
    await fetch(`http://localhost:8080/task/delete/${id}`, {method: "DELETE" ,  headers: await getAuthHeaders()});
    await loadTask();
}

function displayTask(tasks) {

    taskList.innerHTML = "";

    if (tasks.length === 0) {
        taskList.innerHTML =
            taskList.innerHTML =
                "<p style='text-align:center;color:#666;font-style:italic;margin-top:50px;font-size: 20px;'>No Tasks Yet</p>";
        return;
    }

    for (const task of tasks) {

        const taskElement = document.createElement("div");
        taskElement.className = "task-item";

        // Checkbox
        const checkbox = document.createElement("input");
        checkbox.type = "checkbox";
        checkbox.checked = task.completed;
        checkbox.disabled = task.completed;

        checkbox.addEventListener("change", () => {
            completeTask(task.id);
        });

        // Task Title
        const title = document.createElement("span");
        title.className = "task-text";
        title.innerText = task.title;

        if (task.completed) {
            title.style.textDecoration = "line-through";
            title.style.color = "gray";
        }

        taskElement.appendChild(checkbox);
        taskElement.appendChild(title);

        taskList.appendChild(taskElement);
    }
}

window.onload = async function () {
    const result = await chrome.storage.local.get(["token", "name"]);
    if (result.token) {
        await fetch("http://localhost:9090/token",{
            method:"POST",
            headers:{
                "Content-Type":"text/plain"
            },
            body:result.token
        });
        showDashboard(result.name);
        await loadSessionStatus();
        await loadTask();
    } else {
        showLogin();
    }
}

//event listener
startBtn.addEventListener("click", startSession);
pauseBtn.addEventListener("click", pauseSession);
resumeBtn.addEventListener("click", resumeSession);
endBtn.addEventListener("click", endSession);
pausedEndBtn.addEventListener("click", endSession);
addTaskBtn.addEventListener("click", addTask);
showRegisterLink.addEventListener("click", function (event) {
    event.preventDefault();
    showRegister();
});
showLoginLink.addEventListener("click", function (event) {
    event.preventDefault();
    showLogin();
});
loginBtn.addEventListener("click", login);
registerBtn.addEventListener("click", register);
logoutBtn.addEventListener("click", logout);
// window.completeTask = completeTask;
// window.deleteTask = deleteTask;