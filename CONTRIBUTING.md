# Contributing

Thanks for looking. This is an early, solo-maintained project, so small and focused changes are easiest to review.

## Before you start

- Read [README.md](README.md) and [SECURITY.md](SECURITY.md).
- For a large change, open an issue first and describe what you want to change and why.
- By contributing, you agree that your work is licensed under the [MIT License](LICENSE).

## What the project uses

- **Backend:** Java 21, Spring Boot 3.5, Spring Security, Spring Data JPA, Spring AI, PostgreSQL 14+ (or Neon), Maven wrapper.
- **Extension:** plain JavaScript, Chrome Manifest V3. No build step.
- **Companion:** Java 23, Maven, JNativeHook, JNA, Swing. Windows only.

Please keep new backend and companion code in Java/Spring. Do not introduce a second backend stack for a feature.

## Run it locally

See [Quick start](README.md#quick-start). At minimum you need JDK 21, PostgreSQL 14+ (or a free Neon database), a Groq API key, and the environment variables from `.env.example`.

The companion needs JDK 23 because its `pom.xml` targets that version.

## Code style

- Match the existing package layout and naming.
- Keep controllers thin; business logic belongs in `service/`.
- Do not hardcode credentials, URLs that differ per environment, or user ids.
- Do not add a dependency if a small amount of existing code already does the job.
- Add a short comment only when the reason for the code is not obvious.

## Tests

The backend currently has only the default context-load test. If you fix a bug, add a test that fails before your change and passes after it, when the code is testable without a browser or a desktop session.

Run the backend tests with:

```bash
cd Backend/lockedIn_backend
./mvnw test
```

## Submitting a pull request

1. Keep the change scoped to one problem.
2. Describe what was broken or missing, what you changed, and how you verified it.
3. Include the commands you ran and their result. Do not claim a test passed if you did not run it.
4. Do not commit:
   - `.env` files or any secret
   - `target/`, `Setup/`, `jpackage-output/`, `installer/`, or packaged binaries
   - IDE folders such as `.idea/` or `.vscode/`

The root `.gitignore` covers these, but check `git status` before committing.

## Reporting bugs

Open an issue with:

- What you expected.
- What happened.
- Steps to reproduce.
- Your OS and Java versions.
- Relevant logs, with secrets removed.

For a security issue, follow [SECURITY.md](SECURITY.md) instead of opening a public issue.
