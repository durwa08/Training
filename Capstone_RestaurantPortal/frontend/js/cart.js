/**
 * ============================================
 *    Cart Page
 * ============================================
 *
 *  APIs:
 *  GET    /api/cart                            → get cart
 *  PUT    /api/cart/update/{cartItemId}?quantity=N → update qty
 *  DELETE /api/cart/remove/{cartItemId}        → remove one item
 *  DELETE /api/cart/clear                      → clear all
 *
 *  Wallet APIs:
 *  GET    /api/wallet                          → get wallet balance
 *  POST   /api/wallet/add                      → add money to wallet
 */

const FOOD_EMOJIS = [
  "🍕", "🍔", "🍜", "🍣", "🌮", "🍛", "🍱", "🥗",
  "🍗", "🥪", "🧆", "🥘", "🍲", "🥙", "🌯",
];

let currentCart = null;

// ─────────────────────────────────────────
//   PAGE INIT
// ─────────────────────────────────────────
document.addEventListener("DOMContentLoaded", () => {
  requireAuth();
  setupNavbar();
  loadCart();
  loadWalletBalance(); // fetch real wallet balance on load

  document.addEventListener("click", (e) => {
    const menu = document.getElementById("userMenu");
    if (menu && !menu.contains(e.target)) {
      const dd = document.getElementById("userDropdown");
      if (dd) dd.classList.add("hidden");
    }
  });
});

// ─────────────────────────────────────────
//   NAVBAR
// ─────────────────────────────────────────
function setupNavbar() {
  const email = getEmail() || "Account";
  document.getElementById("userEmailNav").textContent = email.split("@")[0];
  document.getElementById("userEmailDrop").textContent = email;
}
function toggleUserMenu() {
  document.getElementById("userDropdown").classList.toggle("hidden");
}

// ─────────────────────────────────────────
//   LOAD CART
//   GET /api/cart
// ─────────────────────────────────────────
async function loadCart() {
  showSkeleton();
  try {
    const res = await apiFetch("/api/cart");

    if (res.status === 404) {
      hideSkeleton();
      showEmpty();
      return;
    }
    if (!res.ok) {
      const err = await res.text();
      throw new Error(err || `Server error ${res.status}`);
    }

    const cart = await res.json();
    currentCart = cart;
    hideSkeleton();

    if (!cart.items || cart.items.length === 0) {
      showEmpty();
      return;
    }
    renderCart(cart);
  } catch (err) {
    console.error("Cart load error:", err);
    hideSkeleton();
    showError(err.message || "Could not load your cart.");
  }
}

// ─────────────────────────────────────────
//   RENDER CART
// ─────────────────────────────────────────
function renderCart(cart) {
  document.getElementById("cartContent").style.display = "";
  document.getElementById("emptyCart").style.display = "none";
  document.getElementById("errorCart").style.display = "none";

  document.getElementById("restaurantBadgeName").textContent =
    cart.restaurantName || "Restaurant";

  const list = document.getElementById("cartItemsList");
  list.innerHTML = "";
  cart.items.forEach((item, idx) => {
    const card = createCartCard(item, idx);
    list.appendChild(card);
  });

  updateSummary(cart);
}

// ─────────────────────────────────────────
//   CREATE CART ITEM CARD
// ─────────────────────────────────────────
function createCartCard(item, index) {
  console.log("Creating card for", item);

  const emoji = FOOD_EMOJIS[item.id % FOOD_EMOJIS.length];
  const delay = index * 60;
  const subtotal = item.price * item.quantity;

  const card = document.createElement("div");
  card.className = "cart-card fade-in";
  card.id = `cart-item-${item.id}`;
  card.style.animationDelay = `${delay}ms`;
  card.style.transition = "opacity .25s, transform .25s";

  card.innerHTML = `
    <div class="cc-emoji">${emoji}</div>
    <div class="cc-info">
      <div class="cc-name">${escapeHtml(item.name)}</div>
      <div class="cc-unit">₹${item.price} each</div>
    </div>
    <div class="cc-qty" id="qty-wrap-${item.id}">
      <button class="cc-qty-btn"
        id="minus-${item.id}"
        onclick="changeQty(${item.id}, -1)"
        ${item.quantity <= 1 ? "disabled" : ""}>−</button>
      <div class="cc-qty-num" id="qty-${item.id}">${item.quantity}</div>
      <button class="cc-qty-btn" onclick="changeQty(${item.id}, 1)">+</button>
    </div>
    <div class="cc-subtotal" id="sub-${item.id}">₹${subtotal.toFixed(2)}</div>
    <button class="cc-remove" onclick="removeItem(${item.id})" title="Remove">✕</button>
  `;

  return card;
}

// ─────────────────────────────────────────
//   UPDATE SUMMARY PANEL
// ─────────────────────────────────────────
function updateSummary(cart) {
  const subtotal = cart.totalAmount || 0;
  const tax = subtotal * 0.05;
  const grandTotal = subtotal + tax;
  const itemCount = (cart.items || []).reduce((s, i) => s + i.quantity, 0);

  document.getElementById("summaryCount").textContent = itemCount;
  document.getElementById("summarySubtotal").textContent = `₹${subtotal.toFixed(2)}`;
  document.getElementById("summaryTax").textContent = `₹${tax.toFixed(2)}`;
  document.getElementById("summaryTotal").textContent = `₹${grandTotal.toFixed(2)}`;
}

// ─────────────────────────────────────────
//   CHANGE QUANTITY
//   POST /api/cart
// ─────────────────────────────────────────
async function changeQty(cartItemId, delta) {
  const item = currentCart.items.find((i) => i.id === cartItemId);

  if (!item) {
    showToast("error", "❌", "Item not found.");
    return;
  }

  const currentQty = item.quantity;
  const newQty = currentQty + delta;
  if (newQty < 1) return;

  console.log("Changing qty for", cartItemId, "from", currentQty, "to", newQty);
  setQtyDisabled(cartItemId, true);

  try {
    const res = await apiFetch("/api/cart", {
      method: "POST",
      body: JSON.stringify({ menuItemId: item.id, quantity: newQty }),
    });

    if (!res.ok) {
      const err = await res.text();
      throw new Error(err || `Error ${res.status}`);
    }

    const updatedCart = await res.json();
    currentCart = updatedCart;

    const updatedItem = updatedCart.items.find((i) => i.id === cartItemId);
    if (updatedItem) {
      document.getElementById(`qty-${cartItemId}`).textContent = updatedItem.quantity;
      document.getElementById(`sub-${cartItemId}`).textContent =
        `₹${(updatedItem.price * updatedItem.quantity).toFixed(2)}`;
      const minusBtn = document.getElementById(`minus-${cartItemId}`);
      if (minusBtn) minusBtn.disabled = updatedItem.quantity <= 1;
    }

    updateSummary(updatedCart);
  } catch (err) {
    showToast("error", "❌", err.message || "Failed to update quantity.");
  } finally {
    setQtyDisabled(cartItemId, false);
  }
}

function setQtyDisabled(cartItemId, disabled) {
  const card = document.getElementById(`cart-item-${cartItemId}`);
  if (!card) return;
  card.querySelectorAll(".cc-qty-btn").forEach((b) => (b.disabled = disabled));
}

// ─────────────────────────────────────────
//   REMOVE ITEM
//   DELETE /api/cart/{cartItemId}
// ─────────────────────────────────────────
async function removeItem(cartItemId) {
  const card = document.getElementById(`cart-item-${cartItemId}`);
  if (card) {
    card.style.opacity = "0";
    card.style.transform = "translateX(30px)";
  }

  try {
    const res = await apiFetch(`/api/cart/${cartItemId}`, { method: "DELETE" });
    if (!res.ok) {
      if (card) {
        card.style.opacity = "1";
        card.style.transform = "";
      }
      const err = await res.text();
      throw new Error(err || `Error ${res.status}`);
    }

    const successMessage = await res.text();

    if (currentCart && currentCart.items) {
      currentCart.items = currentCart.items.filter((item) => item.id !== cartItemId);
      currentCart.totalAmount = currentCart.items.reduce(
        (sum, item) => sum + item.price * item.quantity, 0
      );
    }

    setTimeout(() => { if (card) card.remove(); }, 260);

    if (!currentCart || !currentCart.items || currentCart.items.length === 0) {
      setTimeout(() => {
        document.getElementById("cartContent").style.display = "none";
        showEmpty();
      }, 300);
    } else {
      updateSummary(currentCart);
    }

    showToast("info", "🗑️", successMessage || "Item removed.");
  } catch (err) {
    showToast("error", "❌", err.message || "Failed to remove item.");
  }
}

// ─────────────────────────────────────────
//   CLEAR CART
//   DELETE /api/cart
// ─────────────────────────────────────────
function openClearModal() {
  document.getElementById("modalClear").classList.remove("hidden");
}
function closeClearModal() {
  document.getElementById("modalClear").classList.add("hidden");
}

async function clearCart() {
  closeClearModal();
  try {
    const res = await apiFetch("/api/cart", { method: "DELETE" });
    if (!res.ok) {
      const e = await res.text();
      throw new Error(e || `Error ${res.status}`);
    }

    currentCart = null;
    const cartContent = document.getElementById("cartContent");
    if (cartContent) cartContent.style.display = "none";
    showEmpty();
    showToast("info", "🗑️", "Cart cleared.");
  } catch (err) {
    console.error("Clear cart failed:", err);
    showToast("error", "❌", err.message || "Failed to clear cart.");
  }
}

// ─────────────────────────────────────────
//   PLACE ORDER
//   POST /api/orders
// ─────────────────────────────────────────
async function placeOrder() {
  if (!currentCart || !currentCart.items || currentCart.items.length === 0) {
    showToast("error", "❌", "Your cart is empty.");
    return;
  }

  const address = prompt("Enter delivery address:");
  if (!address || address.trim() === "") {
    showToast("error", "❌", "Delivery address is required.");
    return;
  }

  const phone = prompt("Enter phone number:");
  if (!phone || phone.trim() === "") {
    showToast("error", "❌", "Phone number is required.");
    return;
  }

  try {
    const res = await apiFetch("/api/orders", {
      method: "POST",
      body: JSON.stringify({
        deliveryAddress: address.trim(),
        phoneNumber: phone.trim(),
      }),
    });

    if (!res.ok) {
      const err = await res.text();
      throw new Error(err || `Server error ${res.status}`);
    }

    await res.json();
    currentCart = null;
    document.getElementById("cartContent").style.display = "none";
    showEmpty();
    showToast("success", "✅", "Order placed successfully!");
  } catch (err) {
    console.error("Place order error:", err);
    showToast("error", "❌", err.message || "Failed to place order.");
  }
}

// ─────────────────────────────────────────
//   WALLET — LOAD BALANCE
//   GET /api/wallet
// ─────────────────────────────────────────
async function loadWalletBalance() {
  try {
    const res = await apiFetch("/api/wallet");
    if (!res.ok) return; // silently skip if wallet not available

    const data = await res.json();
    // WalletResponse expected to have a `balance` field
    const balance = data.balance ?? 0;
    updateWalletUI(balance);
  } catch (err) {
    console.warn("Could not load wallet balance:", err);
  }
}

// ─────────────────────────────────────────
//   WALLET — OPEN / CLOSE MODAL
// ─────────────────────────────────────────
function openWalletModal() {
  document.getElementById("walletAmountInput").value = "";
  // clear any previous error highlight
  document.getElementById("walletAmountInput").style.borderColor = "";
  document.getElementById("modalWallet").classList.remove("hidden");
}

function closeWalletModal() {
  document.getElementById("modalWallet").classList.add("hidden");
}

// ─────────────────────────────────────────
//   WALLET — ADD MONEY
//   POST /api/wallet/add   { amount: number }
// ─────────────────────────────────────────
async function addWalletAmount() {
  const input = document.getElementById("walletAmountInput");
  const amount = parseFloat(input.value);

  // client-side validation
  if (!amount || amount <= 0 || isNaN(amount)) {
    input.style.borderColor = "#dc2626";
    setTimeout(() => { input.style.borderColor = ""; }, 1500);
    return;
  }

  // disable button to prevent double-submit
  const addBtn = document.getElementById("walletAddBtn");
  if (addBtn) { addBtn.disabled = true; addBtn.textContent = "Adding…"; }

  try {
    const res = await apiFetch("/api/wallet/add", {
      method: "POST",
      body: JSON.stringify({ amount }),
    });

    if (!res.ok) {
      const err = await res.text();
      throw new Error(err || `Server error ${res.status}`);
    }

    // WalletResponse: { balance: number, ... }
    const data = await res.json();
    const newBalance = data.balance ?? 0;

    updateWalletUI(newBalance);
    closeWalletModal();
    showToast("success", "💰", `₹${amount.toFixed(2)} added to wallet!`);
  } catch (err) {
    console.error("Wallet add error:", err);
    showToast("error", "❌", err.message || "Failed to add wallet amount.");
  } finally {
    if (addBtn) { addBtn.disabled = false; addBtn.textContent = "Add Amount"; }
  }
}

// ─────────────────────────────────────────
//   WALLET — UPDATE UI
// ─────────────────────────────────────────
function updateWalletUI(balance) {
  const walletRow = document.getElementById("walletRow");
  const walletDisplay = document.getElementById("walletBalanceDisplay");

  if (walletRow && walletDisplay) {
    walletRow.style.display = balance > 0 ? "flex" : "none";
    walletDisplay.textContent = `₹${Number(balance).toFixed(2)}`;
  }
}

// ─────────────────────────────────────────
//   SKELETON / STATES
// ─────────────────────────────────────────
function showSkeleton() {
  document.getElementById("skeletonCart").style.display = "";
  document.getElementById("cartContent").style.display = "none";
  document.getElementById("emptyCart").style.display = "none";
  document.getElementById("errorCart").style.display = "none";
}
function hideSkeleton() {
  document.getElementById("skeletonCart").style.display = "none";
}
function showEmpty() {
  document.getElementById("emptyCart").style.display = "";
  document.getElementById("cartContent").style.display = "none";
  document.getElementById("errorCart").style.display = "none";
}
function showError(msg) {
  document.getElementById("errorCart").style.display = "";
  document.getElementById("errorMsg").textContent = msg;
}

// ─────────────────────────────────────────
//   TOAST
// ─────────────────────────────────────────
let toastTimer;
function showToast(type, icon, message) {
  clearTimeout(toastTimer);
  const toast = document.getElementById("toast");
  document.getElementById("toastIcon").textContent = icon;
  document.getElementById("toastMsg").textContent = message;
  toast.className = `show ${type}`;
  toastTimer = setTimeout(() => { toast.className = type; }, 2800);
}

// ─────────────────────────────────────────
//   HELPER
// ─────────────────────────────────────────
function escapeHtml(str) {
  if (!str) return "";
  return String(str)
    .replace(/&/g, "&amp;")
    .replace(/</g, "&lt;")
    .replace(/>/g, "&gt;")
    .replace(/"/g, "&quot;");
}