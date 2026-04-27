import { extractJwtToken, loginUser, registerUser } from "../api.js";
import { isLoggedIn, setToken } from "../auth.js";
import { clearAlert, setDisabled, setHidden, setText, showAlert } from "../ui.js";
import { isEmail, passwordRules, required } from "../validators.js";

export function attachLoginForm({
  form,
  alertBox,
  submitBtn,
  spinner,
  emailInput,
  passwordInput,
  emailMsg,
  passwordMsg,
  onSuccessRedirectTo = "./dashboard.html",
}) {
  function validate() {
    let ok = true;
    clearAlert(alertBox);

    if (!required(emailInput.value)) {
      setText(emailMsg, "Email is required.");
      ok = false;
    } else if (!isEmail(emailInput.value)) {
      setText(emailMsg, "Please enter a valid email address.");
      ok = false;
    } else {
      setText(emailMsg, "");
    }

    if (!required(passwordInput.value)) {
      setText(passwordMsg, "Password is required.");
      ok = false;
    } else {
      setText(passwordMsg, "");
    }

    return ok;
  }

  emailInput.addEventListener("input", validate);
  passwordInput.addEventListener("input", validate);

  form.addEventListener("submit", async (e) => {
    e.preventDefault();
    if (!validate()) return;

    setDisabled(submitBtn, true);
    setHidden(spinner, false);
    clearAlert(alertBox);

    try {
      const payload = {
        email: emailInput.value.trim(),
        password: passwordInput.value,
      };

      const res = await loginUser(payload);
      const token = extractJwtToken(res);
      if (!token) {
        throw new Error(
          "Login succeeded but no JWT was returned. Update extractJwtToken() in js/api.js to match your backend response."
        );
      }

      setToken(token);
      showAlert({ container: alertBox, type: "success", message: "Login successful. Redirecting..." });

      window.setTimeout(() => {
        window.location.href = onSuccessRedirectTo;
      }, 500);
    } catch (err) {
      showAlert({ container: alertBox, type: "error", message: err?.message || "Login failed." });
    } finally {
      setDisabled(submitBtn, false);
      setHidden(spinner, true);
    }
  });
}

export function attachRegisterForm({
  form,
  alertBox,
  submitBtn,
  spinner,
  fullNameInput,
  emailInput,
  passwordInput,
  confirmPasswordInput,
  fullNameMsg,
  emailMsg,
  passwordMsg,
  confirmPasswordMsg,
  passwordHint,
  onSuccess = () => {},
}) {
  function validate() {
    let ok = true;
    clearAlert(alertBox);

    if (!required(fullNameInput.value)) {
      setText(fullNameMsg, "Full name is required.");
      ok = false;
    } else {
      setText(fullNameMsg, "");
    }

    if (!required(emailInput.value)) {
      setText(emailMsg, "Email is required.");
      ok = false;
    } else if (!isEmail(emailInput.value)) {
      setText(emailMsg, "Please enter a valid email address.");
      ok = false;
    } else {
      setText(emailMsg, "");
    }

    const pr = passwordRules(passwordInput.value);
    if (!required(passwordInput.value)) {
      setText(passwordMsg, "Password is required.");
      ok = false;
    } else if (!pr.ok) {
      setText(passwordMsg, "Password must meet all rules below.");
      ok = false;
    } else {
      setText(passwordMsg, "");
    }

    if (passwordHint) {
      setText(passwordHint, "Rules: 8+ chars, uppercase, lowercase, number, special character.");
    }

    if (!required(confirmPasswordInput.value)) {
      setText(confirmPasswordMsg, "Please confirm your password.");
      ok = false;
    } else if (confirmPasswordInput.value !== passwordInput.value) {
      setText(confirmPasswordMsg, "Passwords do not match.");
      ok = false;
    } else {
      setText(confirmPasswordMsg, "");
    }

    return ok;
  }

  fullNameInput.addEventListener("input", validate);
  emailInput.addEventListener("input", validate);
  passwordInput.addEventListener("input", validate);
  confirmPasswordInput.addEventListener("input", validate);

  form.addEventListener("submit", async (e) => {
    e.preventDefault();
    if (!validate()) return;

    setDisabled(submitBtn, true);
    setHidden(spinner, false);
    clearAlert(alertBox);

    try {
      if (isLoggedIn()) {
        // If user already has a token, just treat as success.
        onSuccess();
        return;
      }

      const payload = {
        fullName: fullNameInput.value.trim(),
        email: emailInput.value.trim(),
        password: passwordInput.value,
      };

      await registerUser(payload);
      showAlert({ container: alertBox, type: "success", message: "Registration successful." });
      onSuccess();
    } catch (err) {
      showAlert({ container: alertBox, type: "error", message: err?.message || "Registration failed." });
    } finally {
      setDisabled(submitBtn, false);
      setHidden(spinner, true);
    }
  });
}

