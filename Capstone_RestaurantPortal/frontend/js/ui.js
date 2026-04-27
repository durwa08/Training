/*
  Shortcut for querySelector
  Helps reduce repetitive document.querySelector calls
  Optionally allows searching within a specific root element
*/
export function $(selector, root = document) {
  return root.querySelector(selector);
}

/*
  Safely set text content of an element
  Prevents errors if element is null/undefined
*/
export function setText(el, text) {
  if (!el) return;
  el.textContent = text ?? "";
}

/*
  Show or hide an element using Tailwind's "hidden" class
*/
export function setHidden(el, hidden) {
  if (!el) return;
  el.classList.toggle("hidden", Boolean(hidden));
}

/*
  Enable or disable an element (commonly buttons/inputs)
  Also updates aria-disabled for accessibility
*/
export function setDisabled(el, disabled) {
  if (!el) return;
  el.disabled = Boolean(disabled);
  el.setAttribute("aria-disabled", String(Boolean(disabled)));
}

/*
  Mark element as busy (useful for loading states)
  Helps screen readers understand UI is processing
*/
export function setBusy(el, busy) {
  if (!el) return;
  el.setAttribute("aria-busy", String(Boolean(busy)));
}

/*
  Show alert messages (success, error, info)
  Dynamically applies styles based on alert type
*/
export function showAlert({ container, type = "info", message }) {
  if (!container) return;

  // Tailwind classes for different alert types
  const classes = {
    success: "border-emerald-200 bg-emerald-50 text-emerald-900",
    error: "border-rose-200 bg-rose-50 text-rose-900",
    info: "border-slate-200 bg-slate-50 text-slate-900",
  };

  // Reset classes and apply new styling
  container.className = `hidden w-full rounded-lg border px-4 py-3 text-sm ${
    classes[type] || classes.info
  }`;

  // Set alert message
  container.textContent = message ?? "";

  // Make visible
  container.classList.remove("hidden");
}

/*
  Clear and hide alert container
*/
export function clearAlert(container) {
  if (!container) return;
  container.textContent = "";
  container.classList.add("hidden");
}