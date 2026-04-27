import { isLoggedIn } from "../auth.js";
import { $ } from "../ui.js";
import { attachLoginForm, attachRegisterForm } from "./authForms.js";
import { fetchFeaturedFoodItems } from "../api.js";

function openModal(modal) {
  if (!modal) return;
  modal.classList.remove("hidden");
  document.body.classList.add("overflow-hidden");
}

function closeModal(modal) {
  if (!modal) return;
  modal.classList.add("hidden");
  document.body.classList.remove("overflow-hidden");
}

function swapModal({ from, to }) {
  closeModal(from);
  openModal(to);
}

function wireModal({ modalId, openBtnsSelector, closeBtnsSelector }) {
  const modal = $(modalId);
  document.querySelectorAll(openBtnsSelector).forEach((btn) => {
    btn.addEventListener("click", () => openModal(modal));
  });
  modal?.querySelectorAll(closeBtnsSelector).forEach((btn) => {
    btn.addEventListener("click", () => closeModal(modal));
  });

  // Click outside to close
  modal?.addEventListener("click", (e) => {
    if (e.target === modal) closeModal(modal);
  });

  // Esc to close
  document.addEventListener("keydown", (e) => {
    if (e.key === "Escape") closeModal(modal);
  });

  return modal;
}

function renderFeatured(items) {
  const grid = $("#featuredGrid");
  const loading = $("#featuredLoading");
  const empty = $("#featuredEmpty");
  if (!grid) return;

  if (loading) loading.classList.add("hidden");

  const cleaned = (items || []).slice(0, 6);
  if (!cleaned.length) {
    empty?.classList.remove("hidden");
    return;
  }

  grid.innerHTML = cleaned
    .map((it) => {
      const name = it.name || it.itemName || "Food Item";
      const description = it.description || it.itemDescription || "Delicious and fresh.";
      const price = it.price ?? it.itemPrice;
      const imageUrl =
        it.imageUrl ||
        it.image ||
        "https://images.unsplash.com/photo-1504674900247-0877df9cc836?auto=format&fit=crop&w=1200&q=60";
      const fallbackUrl =
        "https://images.unsplash.com/photo-1540189549336-e6e99c3679fe?auto=format&fit=crop&w=1200&q=60";

      return `
        <article class="group overflow-hidden rounded-2xl border border-white/10 bg-white/5 shadow-lg shadow-black/10 transition hover:-translate-y-0.5 hover:bg-white/10">
          <div class="relative h-40 overflow-hidden">
            <img
              class="h-full w-full object-cover transition duration-500 group-hover:scale-105"
              src="${imageUrl}"
              alt="${name}"
              onerror="this.onerror=null; this.src='${fallbackUrl}';"
            />
            <div class="absolute inset-0 bg-gradient-to-t from-slate-950/70 via-slate-950/10 to-transparent"></div>
            <div class="absolute left-3 top-3 inline-flex items-center gap-1 rounded-full bg-amber-200/90 px-2 py-0.5 text-[11px] font-semibold text-slate-950">
              Chef’s pick
            </div>
          </div>
          <div class="p-4">
            <div class="flex items-start justify-between gap-3">
              <h3 class="text-sm font-semibold text-white">${name}</h3>
              ${
                typeof price === "number"
                  ? `<span class="rounded-full bg-amber-400/90 px-2 py-0.5 text-xs font-semibold text-slate-950">₹${price}</span>`
                  : ""
              }
            </div>
            <p class="mt-1 text-xs text-slate-300">${description}</p>
            <button data-open-login class="mt-3 inline-flex w-full items-center justify-center rounded-lg bg-amber-400 px-3 py-2 text-xs font-semibold text-slate-950 transition hover:bg-amber-300">
              Order now
            </button>
          </div>
        </article>
      `;
    })
    .join("");
}

export function initLandingPage() {
  if (isLoggedIn()) {
    // If already logged in, give the user a direct path.
    const goDash = $("#goDashboard");
    if (goDash) goDash.classList.remove("hidden");
  }

  const loginModal = wireModal({
    modalId: "#loginModal",
    openBtnsSelector: "[data-open-login]",
    closeBtnsSelector: "[data-close-modal]",
  });
  const registerModal = wireModal({
    modalId: "#registerModal",
    openBtnsSelector: "[data-open-register]",
    closeBtnsSelector: "[data-close-modal]",
  });

  // Smooth toggle without full close/re-open by using swap behavior
  document.querySelectorAll("[data-swap-to-register]").forEach((btn) => {
    btn.addEventListener("click", () => swapModal({ from: loginModal, to: registerModal }));
  });
  document.querySelectorAll("[data-swap-to-login]").forEach((btn) => {
    btn.addEventListener("click", () => swapModal({ from: registerModal, to: loginModal }));
  });

  // Login form in modal
  attachLoginForm({
    form: $("#loginFormModal"),
    alertBox: $("#loginAlert"),
    submitBtn: $("#loginSubmitBtn"),
    spinner: $("#loginSpinner"),
    emailInput: $("#loginEmail"),
    passwordInput: $("#loginPassword"),
    emailMsg: $("#loginEmailMsg"),
    passwordMsg: $("#loginPasswordMsg"),
    onSuccessRedirectTo: "./dashboard.html",
  });

  // Register form in modal
  attachRegisterForm({
    form: $("#registerFormModal"),
    alertBox: $("#registerAlert"),
    submitBtn: $("#registerSubmitBtn"),
    spinner: $("#registerSpinner"),
    fullNameInput: $("#regFullName"),
    emailInput: $("#regEmail"),
    passwordInput: $("#regPassword"),
    confirmPasswordInput: $("#regConfirmPassword"),
    fullNameMsg: $("#regFullNameMsg"),
    emailMsg: $("#regEmailMsg"),
    passwordMsg: $("#regPasswordMsg"),
    confirmPasswordMsg: $("#regConfirmPasswordMsg"),
    passwordHint: $("#regPasswordHint"),
    onSuccess: () => swapModal({ from: registerModal, to: loginModal }),
  });

  // Featured items preview (backend or mock)
  fetchFeaturedFoodItems()
    .then((items) => {
      renderFeatured(items);
      // Re-bind any new Order buttons (they use data-open-login)
      document.querySelectorAll("[data-open-login]").forEach((btn) => {
        btn.addEventListener("click", () => openModal(loginModal));
      });
    })
    .catch(() => {
      renderFeatured([]);
    });
}

