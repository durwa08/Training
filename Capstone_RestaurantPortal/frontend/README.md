# Restaurant Portal (Frontend)

This is a **static HTML/CSS/JS frontend** that integrates with your Spring Boot backend.

## Folder structure

```
frontend/
  index.html          # Zosh-style Landing page (Login/Register overlays)
  dashboard.html      # Dashboard (placeholder after login)
  login.html          # User Login page
  register.html       # User Registration page
  styles.css          # Minimal custom CSS (Tailwind does most styling)
  script.js           # Single entrypoint that boots the right page
  js/
    config.js         # API base URL + localStorage key
    auth.js           # JWT token helpers (get/set/clear)
    api.js            # fetch wrapper + register/login calls
    ui.js             # small DOM helpers + alert messages
    validators.js     # email + password validation helpers
    pages/
      loginPage.js
      registerPage.js
      dashboardPage.js
```

## How to run the frontend

Because this uses ES Modules (`<script type="module">`), you should serve it with a small local web server (recommended).

### Option A: VS Code / Cursor “Live Server” extension

- Right click `login.html` → **Open with Live Server**
- It will open something like `http://127.0.0.1:5500/login.html`

### Option B: Python (if installed)

From the `frontend/` folder:

```bash
python -m http.server 5500
```

Then open:
- `http://localhost:5500/` (landing) or `http://localhost:5500/index.html`

## Backend configuration

- Backend base URL is set in `js/config.js`:
  - `API_BASE_URL = http://localhost:8080`
- **Mock fallback** (so UI works even if backend is broken):
  - `MOCK_ON_FAILURE = true`
  - When enabled, Login/Register + Featured foods will use mock responses if the backend is unreachable or errors.

## API integration (Register + Login)

All API calls go through `js/api.js`.

### Register

- **Endpoint**: `POST /api/users/register`
- **Called from**: `js/pages/registerPage.js`
- **Fetch wrapper**: `registerUser(payload)` in `js/api.js`

Payload currently sent:

```json
{
  "fullName": "Jane Doe",
  "email": "jane@example.com",
  "password": "Strong@123"
}
```

If your Spring Boot DTO uses different field names (example: `name`, `username`, etc.), update the payload keys inside `js/pages/registerPage.js`.

### Login

- **Endpoint**: `POST /api/users/login`
- **Called from**: `js/pages/loginPage.js`
- **Fetch wrapper**: `loginUser(payload)` in `js/api.js`

Payload currently sent:

```json
{
  "email": "jane@example.com",
  "password": "Strong@123"
}
```

## JWT storage and usage

### Where it’s stored

After successful login, the JWT is saved to:
- `localStorage["rp_jwt_token"]`

This key name is defined in `js/config.js`.

### How it’s used

For future protected APIs, use:
- `apiFetch("/api/some/protected", { method: "GET", auth: true })`

When `auth: true`, `apiFetch` automatically attaches:
- `Authorization: Bearer <token>`

## Landing overlays (Login/Register) + Dashboard + Logout

- `index.html` is the **landing page** and contains **Login + Register modals** (overlay forms).
- Successful login stores JWT and redirects to `dashboard.html`.
- Registration success switches you to the login modal.

- `dashboard.html` is a placeholder dashboard.
- It checks for a token on load:
  - If token missing → redirects to `index.html`
- Logout button:
  - clears token from localStorage
  - redirects to `index.html`

## Validation + Loading states

- **Validation**: done in `js/validators.js` and applied per-page
  - Required fields
  - Email format
  - Password rules (8+ chars, upper, lower, number, special)
- **Loading states**: submit button disabled + spinner shown during API calls
- **Errors**: backend error responses are parsed and displayed in the alert box

## How to connect future APIs (MenuItem example)

1. Add a new API function in `js/api.js`:

```js
export async function getMenuItems() {
  return apiFetch("/api/menuitems", { method: "GET", auth: true });
}
```

2. Create a page module under `js/pages/` (example: `menuPage.js`) to call `getMenuItems()`.
3. Add a new HTML page (example: `menu.html`) that sets `<body data-page="menu">`.
4. Extend `script.js` to boot that page:

```js
case "menu":
  initMenuPage();
  break;
```

That pattern keeps the frontend scalable as you add:
- restaurant listing
- menu display
- cart/checkout

