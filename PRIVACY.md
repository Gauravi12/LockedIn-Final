# Privacy

LockedIn is a self-hosted focus tracker. When you run it, **you** are the operator of the database. This document describes what the code collects and where it goes.

This is an alpha project. It has not been reviewed by a lawyer or a data protection authority.

## What is collected

| Data | When | Why |
| --- | --- | --- |
| Name, email, password hash | Registration | Account and login |
| Parent email | Registration | Daily report recipient |
| Session start, end, pause, idle seconds | While a session runs | Study-time calculation |
| Distraction count | When content is blocked or you are idle | Daily report |
| Page title and hostname | While a session is active | Content classification |
| Foreground process name | While a session is active (companion) | Desktop app blocking |
| Activity timestamps | Continuously while the companion runs | Idle detection |

The companion records **when** you used the keyboard or mouse. It does not record **which** keys you pressed.

## Where it is stored

- Account, sessions, tasks, and blocked attempts are stored in the **MySQL database you configure**. The default is `localhost:3306/lockedin`.
- The JWT is stored in `chrome.storage.local` in the browser and in memory in the companion.
- The companion does not write activity data to disk. It keeps state in memory.

## What leaves your machine

1. **Groq** receives page titles and process names for classification, and page titles for roast generation. This is the same kind of content you would send to any hosted AI assistant. Do not run a session on pages whose titles you consider sensitive.
2. **Gmail SMTP** delivers the daily report to the parent email saved at registration. The report contains study time, distraction counts, completed and remaining tasks, and top distraction sources.

Nothing else is sent to a third party. There is no analytics, telemetry, advertising, or crash reporting in the code.

## Parent email

Registration asks for a parent or guardian email and sends a daily HTML report there. The address is not verified, and the recipient is not asked for consent. If you deploy this for other people, add consent and an opt-out before collecting real users.

## Retention

Nothing expires automatically. Records stay in your database until you delete them. The content-classification cache is global and permanent in the current version.

To delete a user's data, delete the rows from `user`, `focus_session`, `task`, and `blocked_attempt` for that user. `content_classification` is shared and is not linked to a user.

## Children

The product is aimed at students. If you operate it for minors, you are responsible for obtaining appropriate parental consent in your jurisdiction.

## Changes

This file will change as the project grows. Check the commit history for the current version.

## Contact

Open an issue at <https://github.com/Gauravi12/LockedIn-Final/issues> for privacy questions that do not contain personal data.
