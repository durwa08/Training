import { clearToken, getToken } from "../auth.js";
import { $, setText } from "../ui.js";

function redirectToLogin() {
  window.location.href = "./index.html";
}

export function initDashboardPage() {
  const token = getToken();
  if (!token) {
    redirectToLogin();
    return;
  }

  const tokenPreview = $("#tokenPreview");
  const logoutBtn = $("#logoutBtn");

  setText(tokenPreview, `${token.slice(0, 20)}...${token.slice(-10)}`);

  logoutBtn.addEventListener("click", () => {
    clearToken();
    redirectToLogin();
  });
}

