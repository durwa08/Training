import { registerUser } from "../api.js";
import { isLoggedIn } from "../auth.js";
import { $, clearAlert, setDisabled, setHidden, setText, showAlert } from "../ui.js";
import { isEmail, passwordRules, required } from "../validators.js";

export function initRegisterPage() {
  if (isLoggedIn()) {
    window.location.href = "./dashboard.html";
    return;
  }

  const form = $("#registerForm");
  const alertBox = $("#alert");
  const submitBtn = $("#submitBtn");
  const spinner = $("#spinner");

  const fullName = $("#fullName");
  const email = $("#email");
  const password = $("#password");
  const confirmPassword = $("#confirmPassword");

  const fullNameMsg = $("#fullNameMsg");
  const emailMsg = $("#emailMsg");
  const passwordMsg = $("#passwordMsg");
  const confirmPasswordMsg = $("#confirmPasswordMsg");
  const passwordHint = $("#passwordHint");

  function validate() {
    let ok = true;
    clearAlert(alertBox);

    if (!required(fullName.value)) {
      setText(fullNameMsg, "Full name is required.");
      ok = false;
    } else {
      setText(fullNameMsg, "");
    }

    if (!required(email.value)) {
      setText(emailMsg, "Email is required.");
      ok = false;
    } else if (!isEmail(email.value)) {
      setText(emailMsg, "Please enter a valid email address.");
      ok = false;
    } else {
      setText(emailMsg, "");
    }

    const pr = passwordRules(password.value);
    if (!required(password.value)) {
      setText(passwordMsg, "Password is required.");
      ok = false;
    } else if (!pr.ok) {
      setText(passwordMsg, "Password must meet all rules below.");
      ok = false;
    } else {
      setText(passwordMsg, "");
    }

    setText(
      passwordHint,
      `Rules: 8+ chars, uppercase, lowercase, number, special character.`
    );

    if (!required(confirmPassword.value)) {
      setText(confirmPasswordMsg, "Please confirm your password.");
      ok = false;
    } else if (confirmPassword.value !== password.value) {
      setText(confirmPasswordMsg, "Passwords do not match.");
      ok = false;
    } else {
      setText(confirmPasswordMsg, "");
    }

    return ok;
  }

  fullName.addEventListener("input", validate);
  email.addEventListener("input", validate);
  password.addEventListener("input", validate);
  confirmPassword.addEventListener("input", validate);

  form.addEventListener("submit", async (e) => {
    e.preventDefault();
    if (!validate()) return;

    setDisabled(submitBtn, true);
    setHidden(spinner, false);
    clearAlert(alertBox);

    try {
      // IMPORTANT:
      // If your Spring Boot DTO uses different field names (e.g. username instead of email),
      // update the payload keys here.
      const payload = {
        fullName: fullName.value.trim(),
        email: email.value.trim(),
        password: password.value,
      };

      await registerUser(payload);
      showAlert({
        container: alertBox,
        type: "success",
        message: "Registration successful. Redirecting to login...",
      });

      window.setTimeout(() => {
        window.location.href = "./login.html";
      }, 900);
    } catch (err) {
      showAlert({
        container: alertBox,
        type: "error",
        message: err?.message || "Registration failed. Please try again.",
      });
    } finally {
      setDisabled(submitBtn, false);
      setHidden(spinner, true);
    }
  });
}

