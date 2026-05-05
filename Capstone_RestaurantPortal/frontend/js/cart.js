const BASE_URL = 'http://localhost:8080';

const FOOD_EMOJIS = [
  "🍕", "🍔", "🍜", "🍣", "🌮", "🍛", "🍱", "🥗",
   "🥪", "🧆", "🥘", "🍲", "🥙", "🌯",
];

let currentCart = null;

function getToken() {
    return localStorage.getItem('fm_token');
}

function getEmail() {
    return localStorage.getItem('fm_email');
}

function getUserId() {
    return localStorage.getItem('fm_user_id');
}

function requireAuth() {
    const token = getToken();
    if (!token) {
        window.location.href = 'Auth.html';
        return false;
    }
    return true;
}

function logout() {
    localStorage.removeItem('fm_token');
    localStorage.removeItem('fm_email');
    localStorage.removeItem('fm_role');
    localStorage.removeItem('fm_user_id');
    window.location.href = 'Auth.html';
}

async function apiFetch(path, options = {}) {
    const token = getToken();
    const headers = {
        'Content-Type': 'application/json',
        ...(token ? { 'Authorization': `Bearer ${token}` } : {}),
        ...(options.headers || {}),
    };
    return fetch(`${BASE_URL}${path}`, { ...options, headers });
}

document.addEventListener("DOMContentLoaded", () => {
  requireAuth();
  setupNavbar();
  loadCart();
  loadWalletBalance();

  document.addEventListener("click", (e) => {
    const menu = document.getElementById("userMenu");
    if (menu && !menu.contains(e.target)) {
      const dd = document.getElementById("userDropdown");
      if (dd) dd.classList.add("hidden");
    }
  });
});

function setupNavbar() {
  const email = getEmail() || "Account";
  document.getElementById("userEmailNav").textContent = email.split("@")[0];
  document.getElementById("userEmailDrop").textContent = email;
}

function toggleUserMenu() {
  document.getElementById("userDropdown").classList.toggle("hidden");
}

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

function placeOrder() {
  if (!currentCart || !currentCart.items || currentCart.items.length === 0) {
    showToast("error", "❌", "Your cart is empty.");
    return;
  }
  openOrderModal();
}

function openOrderModal() {
  document.getElementById("orderAddressInput").value = "";
  document.getElementById("orderPhoneInput").value = "";
  document.getElementById("orderAddressInput").style.borderColor = "";
  document.getElementById("orderPhoneInput").style.borderColor = "";
  document.getElementById("modalOrder").classList.remove("hidden");
  setTimeout(() => document.getElementById("orderAddressInput").focus(), 80);
  loadSavedAddresses();
}

function closeOrderModal() {
  document.getElementById("modalOrder").classList.add("hidden");
}

async function submitOrder() {
  const addressInput = document.getElementById("orderAddressInput");
  const phoneInput   = document.getElementById("orderPhoneInput");
  const submitBtn    = document.getElementById("orderSubmitBtn");

  const address = addressInput.value.trim();
  const phone   = phoneInput.value.trim();

  let valid = true;

  if (!address) {
    addressInput.style.borderColor = "#dc2626";
    setTimeout(() => { addressInput.style.borderColor = ""; }, 1500);
    valid = false;
  }
  if (!phone) {
    phoneInput.style.borderColor = "#dc2626";
    setTimeout(() => { phoneInput.style.borderColor = ""; }, 1500);
    valid = false;
  }
  if (!valid) return;

  submitBtn.disabled    = true;
  submitBtn.textContent = "Placing Order…";

  try {
    const res = await apiFetch("/api/orders", {
      method: "POST",
      body: JSON.stringify({
        deliveryAddress: address,
        phoneNumber: phone,
      }),
    });

    if (!res.ok) {
      const err = await res.text();
      throw new Error(err || `Server error ${res.status}`);
    }

    await res.json();
    closeOrderModal();
    currentCart = null;
    document.getElementById("cartContent").style.display = "none";
    showEmpty();
    showToast("success", "✅", "Order placed successfully!");
  } catch (err) {
    console.error("Place order error:", err);
    showToast("error", "❌", err.message || "Failed to place order.");
  } finally {
    submitBtn.disabled    = false;
    submitBtn.textContent = "Confirm Order →";
  }
}

async function loadWalletBalance() {
  try {
    const res = await apiFetch("/api/wallet");
    if (!res.ok) return;

    const data = await res.json();
    const balance = data.balance ?? 0;
    updateWalletUI(balance);
  } catch (err) {
    console.warn("Could not load wallet balance:", err);
  }
}

function openWalletModal() {
  document.getElementById("walletAmountInput").value = "";
  document.getElementById("walletAmountInput").style.borderColor = "";
  document.getElementById("modalWallet").classList.remove("hidden");
}

function closeWalletModal() {
  document.getElementById("modalWallet").classList.add("hidden");
}

async function addWalletAmount() {
  const input = document.getElementById("walletAmountInput");
  const amount = parseFloat(input.value);

  if (!amount || amount <= 0 || isNaN(amount)) {
    input.style.borderColor = "#dc2626";
    setTimeout(() => { input.style.borderColor = ""; }, 1500);
    return;
  }

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

function updateWalletUI(balance) {
  const walletRow = document.getElementById("walletRow");
  const walletDisplay = document.getElementById("walletBalanceDisplay");

  if (walletRow && walletDisplay) {
    walletRow.style.display = balance > 0 ? "flex" : "none";
    walletDisplay.textContent = `₹${Number(balance).toFixed(2)}`;
  }
}

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

let toastTimer;
function showToast(type, icon, message) {
  clearTimeout(toastTimer);
  const toast = document.getElementById("toast");
  document.getElementById("toastIcon").textContent = icon;
  document.getElementById("toastMsg").textContent = message;
  toast.className = `show ${type}`;
  toastTimer = setTimeout(() => { toast.className = type; }, 2800);
}

function escapeHtml(str) {
  if (!str) return "";
  return String(str)
    .replace(/&/g, "&amp;")
    .replace(/</g, "&lt;")
    .replace(/>/g, "&gt;")
    .replace(/"/g, "&quot;");
}

let savedAddresses = [];
let selectedAddressId = null;

async function loadSavedAddresses() {
  const userId = getUserId();

  const section  = document.getElementById("savedAddressesSection");
  const skeleton = document.getElementById("addrSkeleton");
  const list     = document.getElementById("savedAddressList");

  selectedAddressId = null;
  section.style.display  = "none";
  skeleton.style.display = "block";
  list.innerHTML         = "";
  hideAddAddressForm();

  if (!userId) {
    skeleton.style.display = "none";
    return;
  }

  try {
    const res = await apiFetch(`/api/addresses/${userId}`);
    if (!res.ok) throw new Error("Failed to load addresses");

    savedAddresses = await res.json();
    skeleton.style.display = "none";

    if (savedAddresses.length > 0) {
      section.style.display = "block";
      savedAddresses.forEach(addr => renderAddressCard(addr, list));
    }
  } catch (err) {
    skeleton.style.display = "none";
    console.warn("Address load error:", err);
  }
}

function renderAddressCard(addr, container) {
  const card = document.createElement("div");
  card.id = `addr-card-${addr.id}`;
  card.style.cssText = "display:flex;align-items:flex-start;gap:10px;padding:10px 12px;border:1.5px solid #e5e7eb;border-radius:10px;cursor:pointer;transition:all .18s;background:#fff;font-family:'DM Sans',sans-serif;";

  const addrText = `${addr.street}, ${addr.city}, ${addr.state} — ${addr.pincode}`;

  card.innerHTML = `
    <div style="margin-top:2px;font-size:18px;">📍</div>
    <div style="flex:1;min-width:0;">
      <div style="font-size:13px;font-weight:500;color:#111827;word-break:break-word;">${escapeHtml(addrText)}</div>
    </div>
    <button
      onclick="deleteAddress(event,${addr.id})"
      title="Delete"
      style="flex-shrink:0;background:none;border:none;cursor:pointer;font-size:15px;color:#9ca3af;padding:2px 4px;border-radius:6px;"
      onmouseover="this.style.color='#dc2626'"
      onmouseout="this.style.color='#9ca3af'"
    >✕</button>
  `;

  card.addEventListener("click", (e) => {
    if (e.target.closest("button")) return;
    selectAddressCard(addr, card, addrText);
  });

  container.appendChild(card);
}

function selectAddressCard(addr, cardEl, addrText) {
  document.querySelectorAll("[id^='addr-card-']").forEach(c => {
    c.style.borderColor = "#e5e7eb";
    c.style.background  = "#fff";
    c.style.boxShadow   = "none";
  });

  cardEl.style.borderColor = "#10b981";
  cardEl.style.background  = "#f0fdf4";
  cardEl.style.boxShadow   = "0 0 0 3px rgba(16,185,129,.15)";

  selectedAddressId = addr.id;
  document.getElementById("orderAddressInput").value = addrText;
}

function showAddAddressForm() {
  document.getElementById("addAddressForm").style.display = "block";
  document.getElementById("showAddAddressBtn").style.display = "none";
  ["addrStreet","addrCity","addrState","addrPincode"].forEach(id => {
    document.getElementById(id).value = "";
    document.getElementById(id).style.borderColor = "";
  });
}

function hideAddAddressForm() {
  document.getElementById("addAddressForm").style.display  = "none";
  const btn = document.getElementById("showAddAddressBtn");
  if (btn) btn.style.display = "";
}

async function saveNewAddress() {
  const userId  = getUserId();
  if (!userId) return;

  const street  = document.getElementById("addrStreet").value.trim();
  const city    = document.getElementById("addrCity").value.trim();
  const state   = document.getElementById("addrState").value.trim();
  const pincode = document.getElementById("addrPincode").value.trim();

  let valid = true;
  [["addrStreet",street],["addrCity",city],["addrState",state],["addrPincode",pincode]].forEach(([id, val]) => {
    const el = document.getElementById(id);
    if (!val) { el.style.borderColor = "#dc2626"; valid = false; }
    else        el.style.borderColor = "";
  });

  if (!/^\d{6}$/.test(pincode)) {
    document.getElementById("addrPincode").style.borderColor = "#dc2626";
    showToast("error", "❌", "Pincode must be exactly 6 digits.");
    return;
  }
  if (!valid) return;

  const btn = document.getElementById("saveAddrBtn");
  btn.disabled    = true;
  btn.textContent = "Saving…";

  try {
    const res = await apiFetch(`/api/addresses/${userId}`, {
      method: "POST",
      body: JSON.stringify({ street, city, state, pincode }),
    });

    if (!res.ok) {
      const err = await res.text();
      throw new Error(err || `Error ${res.status}`);
    }

    const newAddr = await res.json();
    savedAddresses.push(newAddr);

    const list = document.getElementById("savedAddressList");
    renderAddressCard(newAddr, list);
    document.getElementById("savedAddressesSection").style.display = "block";

    hideAddAddressForm();
    showToast("success", "📍", "Address saved!");
  } catch (err) {
    showToast("error", "❌", err.message || "Failed to save address.");
  } finally {
    btn.disabled    = false;
    btn.textContent = "Save Address";
  }
}

async function deleteAddress(event, addressId) {
  event.stopPropagation();
  const userId = getUserId();
  if (!userId) return;

  const card = document.getElementById(`addr-card-${addressId}`);
  if (card) { card.style.opacity = "0.4"; card.style.pointerEvents = "none"; }

  try {
    const res = await apiFetch(`/api/addresses/${userId}/${addressId}`, { method: "DELETE" });
    if (!res.ok) throw new Error(`Error ${res.status}`);

    savedAddresses = savedAddresses.filter(a => a.id !== addressId);
    if (card) card.remove();

    if (selectedAddressId === addressId) {
      selectedAddressId = null;
      document.getElementById("orderAddressInput").value = "";
    }

    if (savedAddresses.length === 0) {
      document.getElementById("savedAddressesSection").style.display = "none";
    }

    showToast("info", "🗑️", "Address removed.");
  } catch (err) {
    if (card) { card.style.opacity = "1"; card.style.pointerEvents = ""; }
    showToast("error", "❌", err.message || "Failed to delete address.");
  }
}