# Security

## Supported versions

Only the latest commit on `main` is supported. This is an alpha project; there are no long-term support branches.

The reference deployment at `https://lockedin-6k2g.onrender.com` is a shared demo maintained by the project author. It is not a private instance. It has no rate limiting, so anyone who finds it can create accounts and spend the maintainer's Groq quota. Do not use it for sensitive data.

## Reporting a vulnerability

Please **do not open a public issue** for an exploitable bug. Email the maintainer through the contact on <https://github.com/Gauravi12> or use GitHub's private vulnerability reporting on the repository.

Include:

- What you found and where (file, endpoint).
- Steps to reproduce.
- What an attacker could do with it.
- Any suggested fix.

You will get an acknowledgement when the report is seen. This is a solo-maintained project, so a fix may take time.

## Known security limitations

These are known and tracked in the README. They are not secrets:

1. **Logout does not revoke the JWT.** A token remains valid until its 24-hour expiry. Anyone who copies it can use it.
2. **No rate limiting.** `/auth/login`, `/auth/register`, and `/content/check` can be called as fast as the server allows. `/content/check` spends Groq quota.
3. **No email verification or password reset.** Anyone can register with any email address.
4. **The companion token endpoint (`:9090/token`) has no authentication** and binds to all interfaces. Any process or device that can reach port 9090 can replace the token.
5. **The companion can kill processes** with `taskkill /F /IM`. It decides based on the process name returned by the AI, so a misclassification can close an unrelated app.
6. **Raw page titles are stored indefinitely** in a global cache and are not scoped per user.
7. **CORS is open** (`@CrossOrigin(origins = "*")`) on session and content controllers. Authentication still requires a bearer token, but the permissive CORS policy should be tightened for a hosted deployment.
8. **The default profile has no HTTPS.** A hosted deployment must terminate TLS in front of the backend.

## If you deploy this

Before putting it on the public internet:

- Put HTTPS (Caddy, nginx, or a managed load balancer) in front of the backend.
- Change `JWT_SECRET` to a random value of at least 32 characters and keep it out of git.
- Restrict CORS to your extension origin.
- Add rate limiting at the proxy or in the application.
- Do not expose port 9090.
- Use a dedicated database user with access only to the `lockedin` schema.
- Use a Gmail **app password**, and rotate it if it leaks.
- Monitor Groq usage; a public unauthenticated endpoint can burn your quota.
- Review the process-killing code path before letting anyone run the companion.

## Secrets

No secrets belong in this repository. `application.properties` reads credentials from environment variables. If you accidentally commit a secret, rotate it immediately; removing the commit is not enough.
