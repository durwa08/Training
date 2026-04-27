import { loginUser, extractJwtToken } from "../api.js";
import { setToken, isLoggedIn } from "../auth.js";
import { $, clearAlert, setDisabled, setHidden, setText, showAlert } from "../ui.js";
import { isEmail, required } from "../validators.js";

export function initLoginPage() {
  if (isLoggedIn()) {
    window.location.href = "./dashboard.html";
    return;
  }

  const form = $("#loginForm");
  const alertBox = $("#alert");
  const submitBtn = $("#submitBtn");
  const spinner = $("#spinner");

  const email = $("#email");
  const password = $("#password");
  const emailMsg = $("#emailMsg");
  const passwordMsg = $("#passwordMsg");

  function validate() {
    let ok = true;
    clearAlert(alertBox);

    if (!required(email.value)) {
      setText(emailMsg, "Email is required.");
      ok = false;
    } else if (!isEmail(email.value)) {
      setText(emailMsg, "Please enter a valid email address.");
      ok = false;
    } else {
      setText(emailMsg, "");
    }

    if (!required(password.value)) {
      setText(passwordMsg, "Password is required.");
      ok = false;
    } else {
      setText(passwordMsg, "");
    }

    return ok;
  }

  email.addEventListener("input", validate);
  password.addEventListener("input", validate);

  form.addEventListener("submit", async (e) => {
    e.preventDefault();
    if (!validate()) return;

    setDisabled(submitBtn, true);
    setHidden(spinner, false);
    clearAlert(alertBox);

    try {
      const payload = {
        email: email.value.trim(),
        password: password.value,
      };

      const res = await loginUser(payload);
      const token = extractJwtToken(res);

      if (!token) {
        throw new Error(
          "Login succeeded but no JWT was returned. Update extractJwtToken() to match your backend response shape."
        );
      }

      setToken(token);
      showAlert({ container: alertBox, type: "success", message: "Login successful. Redirecting..." });

      window.setTimeout(() => {
        window.location.href = "./dashboard.html";
      }, 600);
    } catch (err) {
      showAlert({
        container: alertBox,
        type: "error",
        message: err?.message || "Login failed. Please try again.",
      });
    } finally {
      setDisabled(submitBtn, false);
      setHidden(spinner, true);
    }
  });
}

