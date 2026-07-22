# 🚀 LockedIn - AI Powered Focus Companion

## 📌 Overview

LockedIn is an AI-powered productivity system that helps students stay focused during study sessions.

The system monitors the websites and videos a user opens. If distracting content is detected during an active study session, LockedIn interrupts the distraction with motivational roast messages and records distraction time.

Unlike traditional website blockers, LockedIn uses AI to understand the content instead of blocking entire websites. This allows educational content on platforms like YouTube while blocking entertainment content.

---

# ✨ Features

### 🔐 Secure Authentication

* User Registration & Login
* JWT Authentication
* Password Encryption using BCrypt
* Role-Based Access (USER)

---

### 📚 Focus Sessions

Users can

* Start Study Session
* Pause Session
* Resume Session
* End Session

During an active session the system tracks

* Study Time
* Distraction Time
* Sleep/Idle Time

---

### 🤖 AI Content Classification

Every opened page is sent to the backend where an AI model classifies it as

* Study Content ✅
* Distracting Content ❌

Examples

Allowed

* DSA videos
* DBMS tutorials
* Spring Boot courses
* Documentation

Blocked

* Entertainment videos
* Shorts
* Instagram Reels
* Meme pages
* Gaming videos

---

### 😂 AI Roast Popup

If distracting content is detected, a popup appears with an AI-generated roast message encouraging the user to return to studying.

Example

> "Your future is loading... but you're buffering on cat videos."

---

### 💻 Desktop Companion

A Java Desktop Companion monitors

* User Idle Time
* System Activity

If the user is inactive

* Idle time is tracked
* Study statistics are updated automatically

---

### 📧 Daily Study Report

At the end of every day an automatic email report is sent.

The report includes

* Total Study Time
* Total Distraction Time
* Total Idle Time
* Tasks Completed
* Productivity Summary

---

### ✅ Task Management

Users can

* Add Tasks
* Complete Tasks
* Delete Tasks
* Track Daily Progress

---

# 🏗️ Project Architecture

```
Chrome Extension
        │
        ▼
Spring Boot Backend
        │
        ▼
AI Content Classification
        │
        ▼
MySQL Database
        │
        ▼
Desktop Companion
        │
        ▼
Daily Email Reports
```

---

# 🛠️ Tech Stack

## Backend

* Java
* Spring Boot
* Spring Security
* Spring Data JPA
* JWT Authentication
* Maven

---

## Database

* MySQL

---

## AI

* Groq API
* Llama 3.3 70B

---

## Desktop

* Java

---

## Browser

* Chrome Extension (Manifest V3)

---

## Email

* Java Mail Sender
* Gmail SMTP

---

# 📂 Project Structure

```
LockedIn

├── Backend
│   └── Spring Boot Backend
│
├── Desktop Companion
│   └── Java Desktop Application
│
└── Extension
    └── Chrome Extension
```

---

# 🔄 Workflow

1. User logs into LockedIn.
2. User starts a study session.
3. Chrome Extension monitors opened pages.
4. Page information is sent to the backend.
5. AI classifies the content.
6. Educational content is allowed.
7. Distracting content triggers a roast popup.
8. Distraction time is recorded.
9. Desktop Companion tracks idle time.
10. A daily productivity report is emailed automatically.

---

# 🔒 Security

* JWT Authentication
* BCrypt Password Encryption
* Environment Variables for Secrets
* Protected REST APIs
* Role-Based Authorization

---

# 🌟 Future Improvements

* Windows Installer
* Automatic Startup
* Parent Dashboard
* Mobile Application
* Productivity Analytics Dashboard
* Cross-device Synchronization

---

# 👩‍💻 Author

**Gauravi Jadhav**

Final Year Engineering Student

Backend Developer | Java | Spring Boot | AI

---

## One more thing, bro 👀

Your repository currently shows:

```
.idea/
Backend/
Desktop Companion/
Extension/
```

I would also add a small section like this at the top:

> **Status:** ✅ Fully Functional Prototype

or

> **Current Version:** v1.0 (Final Year Project)
> <img width="959" height="509" alt="Screenshot 2026-07-22 120105" src="https://github.com/user-attachments/assets/2580d457-b981-4c5f-aea7-7918a7b952e7" />

> <img width="959" height="519" alt="Screenshot 2026-07-22 120032" src="https://github.com/user-attachments/assets/7736c0e7-1ec6-4d56-8a92-416caeaa4bf8" />

><img width="322" height="467" alt="Screenshot 2026-07-22 115958" src="https://github.com/user-attachments/assets/cb052e20-03bb-4bcd-a2ac-9d61e0570662" />

><img width="317" height="452" alt="Screenshot 2026-07-22 115944" src="https://github.com/user-attachments/assets/d3d68837-4407-4bfd-bf6f-3a839072a491" />

> <img width="322" height="458" alt="Screenshot 2026-07-22 115929" src="https://github.com/user-attachments/assets/f1b1fe9d-e922-47b4-9eb1-f12252a555f5" />

> <img width="317" height="210" alt="Screenshot 2026-07-22 115826" src="https://github.com/user-attachments/assets/30626b65-4516-46a4-92e5-6bd4ead632e9" />
