/**
 * ============================================
 *   OWNER ORDERS PAGE
 * ============================================
 */

// ─────────────────────────────────────────
//   PAGE INIT
// ─────────────────────────────────────────
document.addEventListener("DOMContentLoaded", () => {
  requireAuth(); // from utils.js
  loadOrders();
});

// ─────────────────────────────────────────
//   LOAD ORDERS (GET /api/orders/owner)
// ─────────────────────────────────────────
async function loadOrders() {
  try {
    const res = await apiFetch("/api/orders/owner");

    if (!res.ok) throw new Error("Failed to fetch orders");

    const orders = await res.json();

    renderOrders(orders);
  } catch (err) {
    console.error(err);
    showToast("error", "❌", "Failed to load orders");
  }
}

// ─────────────────────────────────────────
//   RENDER ORDERS TABLE & UPDATE STATS
// ─────────────────────────────────────────
function renderOrders(orders) {
  const tbody = document.getElementById("ordersTbody");
  tbody.innerHTML = "";

  if (!orders || orders.length === 0) {
    tbody.innerHTML = `
            <tr>
                <td colspan="5" class="text-center py-6 text-ash">
                    No orders found
                </td>
            </tr>`;
    updateOrderStats(orders);
    return;
  }

  orders.forEach((order) => {
    tbody.appendChild(createOrderRow(order));
  });

  updateOrderStats(orders);
}

// ─────────────────────────────────────────
//   UPDATE STATS CARDS
// ─────────────────────────────────────────
function updateOrderStats(orders) {
  if (!orders) orders = [];

  const total = orders.length;
  const pending = orders.filter((o) => o.status === "PENDING" || o.status === "PLACED").length;
  const completed = orders.filter((o) => o.status === "COMPLETED" || o.status === "DELIVERED").length;
  const cancelled = orders.filter((o) => o.status === "CANCELLED").length;

  document.getElementById("totalOrders").textContent = total;
  document.getElementById("pendingOrders").textContent = pending;
  document.getElementById("completedOrders").textContent = completed;
  document.getElementById("cancelledOrders").textContent = cancelled;
}

function createOrderRow(order) {
  const orderId = order.orderId || order.id;
  const tr = document.createElement("tr");
  tr.dataset.orderId = orderId;

  tr.innerHTML = `
            <td class="font-semibold text-center">#${orderId}</td>

            <td class="text-center">${order.customerName || "User"}</td>

            <td class="font-semibold text-emerald text-center order-total-cell">
                ₹${Number(order.totalAmount || 0).toFixed(2)}
            </td>

            <td class="text-center order-status-cell">
                <span class="status-badge status-${order.status}">
                    ${getStatusIcon(order.status)} ${order.status}
                </span>
            </td>

            <td class="text-center order-action-cell">
                ${renderActionButtons(orderId, order)}
            </td>
        `;

  return tr;
}

function updateOrderRow(order) {
  const orderId = order.orderId || order.id;
  const existingRow = document.querySelector(`tr[data-order-id="${orderId}"]`);
  if (!existingRow) {
    document.getElementById("ordersTbody").appendChild(createOrderRow(order));
    return;
  }

  const statusCell = existingRow.querySelector(".order-status-cell");
  const actionCell = existingRow.querySelector(".order-action-cell");
  const totalCell = existingRow.querySelector(".order-total-cell");

  if (totalCell) {
    totalCell.innerHTML = `₹${Number(order.totalAmount || 0).toFixed(2)}`;
  }
  if (statusCell) {
    statusCell.innerHTML = `
                <span class="status-badge status-${order.status}">
                    ${getStatusIcon(order.status)} ${order.status}
                </span>`;
  }
  if (actionCell) {
    actionCell.innerHTML = renderActionButtons(orderId, order);
  }
}

// ─────────────────────────────────────────
//   STATUS ICONS
// ─────────────────────────────────────────
function getStatusIcon(status) {
  switch (status) {
    case "PLACED":
      return "🆕";
    case "PENDING":
      return "⏳";
    case "DELIVERED":
      return "🚚";
    case "COMPLETED":
      return "✅";
    case "CANCELLED":
      return "❌";
    default:
      return "";
  }
}

// ─────────────────────────────────────────
//   ACTION BUTTONS BASED ON STATUS
// ─────────────────────────────────────────
function renderActionButtons(orderId, order) {
  const btnAccept = `<button onclick="updateOrderStatus(${orderId}, 'PENDING')" class="px-4 py-2 bg-emerald text-white rounded-lg font-semibold text-sm hover:bg-emerald-dark transition">Accept</button>`;
  const btnCancel = `<button onclick="updateOrderStatus(${orderId}, 'CANCELLED')" class="px-4 py-2 bg-red-500 text-white rounded-lg font-semibold text-sm hover:bg-red-600 transition">Cancel</button>`;
  const btnDeliver = `<button onclick="updateOrderStatus(${orderId}, 'DELIVERED')" class="px-4 py-2 bg-blue-500 text-white rounded-lg font-semibold text-sm hover:bg-blue-600 transition">Mark Delivered</button>`;
  const btnComplete = `<button onclick="updateOrderStatus(${orderId}, 'COMPLETED')" class="px-4 py-2 bg-emerald text-white rounded-lg font-semibold text-sm hover:bg-emerald-dark transition">Complete</button>`;

  switch (order.status) {
    case "PLACED":
      return `<div class="flex gap-2 justify-center flex-wrap">${btnAccept}${btnCancel}</div>`;

    case "PENDING":
      return `<div class="flex gap-2 justify-center flex-wrap">${btnDeliver}</div>`;

    case "DELIVERED":
      return `<span class="inline-block px-3 py-2 bg-green-100 text-green-700 rounded-lg font-semibold text-sm">✔ Done</span>`;

    case "COMPLETED":
      return `<span class="inline-block px-3 py-2 bg-green-100 text-green-700 rounded-lg font-semibold text-sm">✔ Done</span>`;

    case "CANCELLED":
      return `<span class="inline-block px-3 py-2 bg-red-100 text-red-700 rounded-lg font-semibold text-sm">✖ Cancelled</span>`;

    default:
      return "";
  }
}

// ─────────────────────────────────────────
//   UPDATE ORDER STATUS (PUT API)
//   PUT /api/orders/${orderId}?status=${status}
// ─────────────────────────────────────────
async function updateOrderStatus(orderId, status) {
  try {
    const res = await apiFetch(`/api/orders/${orderId}?status=${status}`, {
      method: "PUT",
    });

    if (!res.ok) {
      const err = await res.text();
      throw new Error(err || "Update failed");
    }

    const updatedOrder = await res.json();
    showToast("success", "✅", `Order ${status}`);

    // Reload all orders to reflect changes immediately
    await loadOrders();
  } catch (err) {
    showToast("error", "❌", err.message || "Failed to update order");
  }
}

// ─────────────────────────────────────────
//   BACK BUTTON
// ─────────────────────────────────────────
function goBack() {
  window.location.href = "owner-dashboard.html";
}
