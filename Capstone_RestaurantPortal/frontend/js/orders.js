/**
 *  Orders
 * Shows order history + cancel within 30 seconds
 */

let allOrders = [];

document.addEventListener("DOMContentLoaded", () => {
  requireAuth();
  loadOrders();
});

// ── LOAD ORDERS ────────────────────────────

async function loadOrders() {
  try {
    /**
     * GET /api/orders
     * Returns all orders for logged in customer
     * Sorted by newest first
     */
    const res = await apiFetch("/api/orders");
    const data = await res.json();

    document.getElementById("ordersLoading").classList.add("hidden");

    if (!Array.isArray(data) || data.length === 0) {
      document.getElementById("ordersEmpty").classList.remove("hidden");
      return;
    }

    allOrders = data;
    // Sort by newest first
    data.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));
    document.getElementById("ordersList").classList.remove("hidden");
    renderOrders(data);
  } catch (err) {
    console.error("Orders load error:", err);
    document.getElementById("ordersLoading").classList.add("hidden");
    document.getElementById("ordersEmpty").classList.remove("hidden");
  }
}

// ── RENDER ORDERS ──────────────────────────

function renderOrders(orders) {
  const list = document.getElementById("ordersList");
  list.innerHTML = "";

  orders.forEach((order) => {
    const card = createOrderCard(order);
    list.appendChild(card);
  });
}

function createOrderCard(order) {
  const card = document.createElement("div");
  card.className = "order-card";
  card.id = `order-${order.orderId}`;

  const canCancel =
    order.status === "PENDING" && isWithin30Seconds(order.createdAt);
  const date = formatDate(order.createdAt);

  // If can cancel, set up auto-hide timer
  if (canCancel) {
    const created = new Date(order.createdAt);
    const now = new Date();
    const diff = (now - created) / 1000;
    const remaining = Math.max(0, 30 - diff);
    setTimeout(() => {
      const btn = document.getElementById(`cancel-btn-${order.orderId}`);
      if (btn) btn.style.display = "none";
      const countdown = document.getElementById(`countdown-${order.orderId}`);
      if (countdown) countdown.style.display = "none";
    }, remaining * 1000);
  }

  // Build items list
  const itemsHtml = (order.items || [])
    .map(
      (item) =>
        `<div class="flex justify-between text-sm font-dm py-1">
            <span class="text-ink">${escapeHtml(item.menuItemName)} <span class="text-ash">x${item.quantity}</span></span>
            <span class="text-ash">₹${(item.price * item.quantity).toFixed(2)}</span>
        </div>`,
    )
    .join("");

  card.innerHTML = `
        <!-- Header -->
        <div class="p-5 border-b border-gold/10 flex items-start justify-between gap-4 flex-wrap">
            <div>
                <div class="flex items-center gap-3 mb-1">
                    <span class="font-playfair text-lg font-bold text-ink">Order #${order.orderId}</span>
                    <span class="status-badge status-${order.status}">
                        ${getStatusIcon(order.status)} ${order.status}
                    </span>
                </div>
                <p class="text-ash text-sm font-dm">📅 ${date}</p>
                <p class="text-ash text-sm font-dm">📍 ${escapeHtml(order.deliveryAddress || "N/A")}</p>
                <p class="text-ash text-sm font-dm">📞 ${escapeHtml(order.phoneNumber || "N/A")}</p>
            </div>
            <div class="text-right">
                <p class="font-playfair text-2xl font-bold text-burgundy">₹${order.totalAmount.toFixed(2)}</p>
                ${
                  canCancel
                    ? `<button id="cancel-btn-${order.orderId}" class="cancel-btn mt-2" onclick="cancelOrder(${order.orderId})">
                        Cancel Order
                       </button>`
                    : ""
                }
            </div>
        </div>

        <!-- Items -->
        <div class="p-5">
            <p class="text-xs font-semibold text-ash uppercase tracking-wider mb-3 font-dm">Items Ordered</p>
            ${itemsHtml}
            <div class="border-t border-gold/10 mt-3 pt-3 flex justify-between">
                <span class="font-semibold text-ink font-dm">Total</span>
                <span class="font-playfair font-bold text-burgundy">₹${order.totalAmount.toFixed(2)}</span>
            </div>
        </div>

        <!-- Cancel countdown if PENDING -->
        ${
          canCancel
            ? `<div class="bg-blue-50 border-t border-blue-100 px-5 py-3" id="countdown-${order.orderId}">
                <p class="text-xs text-blue-600 font-dm font-medium">
                    ⏱️ You can cancel this order within 30 seconds of placing it.
                </p>
               </div>`
            : ""
        }
    `;

  return card;
}

// CANCEL ORDER

async function cancelOrder(orderId) {
  if (
    !confirm("Cancel this order? The amount will be refunded to your wallet.")
  )
    return;

  try {
    /**
     * DELETE /api/orders/{orderId}/cancel
     * Returns updated order with status CANCELLED
     */
    const res = await apiFetch(`/api/orders/${orderId}/cancel`, {
      method: "PUT",
    });

    const data = await res.text();

    if (!res.ok) {
      showToast("error", "❌", data || "Could not cancel order");
      return;
    }

    showToast("success", "✅", "Order cancelled. Amount refunded to wallet!");

    const card = document.getElementById(`order-${orderId}`);
    if (card) {
      const statusBadge = card.querySelector(".status-badge");
      if (statusBadge) {
        statusBadge.className = "status-badge status-CANCELLED";
        statusBadge.innerHTML = `${getStatusIcon("CANCELLED")} CANCELLED`;
      }
      const btn = document.getElementById(`cancel-btn-${orderId}`);
      if (btn) btn.style.display = "none";
      const countdown = document.getElementById(`countdown-${orderId}`);
      if (countdown) countdown.style.display = "none";
    }
  } catch (err) {
    showToast("error", "❌", "Could not cancel order. Please try again.");
  }
}



function isWithin30Seconds(createdAt) {
  if (!createdAt) return false;
  const created = new Date(createdAt);
  const now = new Date();
  const diff = (now - created) / 1000; // seconds
  return diff <= 30;
}

function formatDate(dateStr) {
  if (!dateStr) return "--";
  try {
    const d = new Date(dateStr);
    return d.toLocaleDateString("en-IN", {
      day: "numeric",
      month: "short",
      year: "numeric",
      hour: "2-digit",
      minute: "2-digit",
    });
  } catch {
    return dateStr;
  }
}

function getStatusIcon(status) {
  const icons = {
    PLACED: "🔵",
    PENDING: "🟠",
    DELIVERED: "🟢",
    COMPLETED: "✅",
    CANCELLED: "🔴",
  };
  return icons[status] || "⚪";
}

function escapeHtml(str) {
  if (!str) return "";
  return String(str)
    .replace(/&/g, "&amp;")
    .replace(/</g, "&lt;")
    .replace(/>/g, "&gt;");
}


let toastTimer;
function showToast(type, icon, msg) {
  clearTimeout(toastTimer);
  const t = document.getElementById("toast");
  document.getElementById("toastIcon").textContent = icon;
  document.getElementById("toastMsg").textContent = msg;
  t.className = `show ${type}`;
  toastTimer = setTimeout(() => (t.className = type), 2800);
}
