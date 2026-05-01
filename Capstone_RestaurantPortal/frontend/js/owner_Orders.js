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
//   RENDER ORDERS TABLE
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
    return;
  }

  orders.forEach((order) => {
    tbody.appendChild(createOrderRow(order));
  });
}

function createOrderRow(order) {
  const orderId = order.orderId || order.id;
  const tr = document.createElement("tr");
  tr.dataset.orderId = orderId;

  tr.innerHTML = `
            <td class="font-semibold">#${orderId}</td>

            <td>${order.customerName || "User"}</td>

            <td class="font-semibold text-emerald order-total-cell">
                ₹${Number(order.totalAmount || 0).toFixed(2)}
            </td>

            <td class="order-status-cell">
                <span class="status-badge status-${order.status}">
                    ${getStatusIcon(order.status)} ${order.status}
                </span>
            </td>

            <td class="order-action-cell">
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
  switch (order.status) {
    case "PLACED":
      return `
                <button onclick="updateOrderStatus(${orderId}, 'PENDING')" class="btn-secondary-sm">
                    Accept
                </button>
                <button onclick="updateOrderStatus(${orderId}, 'CANCELLED')" class="btn-danger-sm">
                    Cancel
                </button>
            `;

    case "PENDING":
      return `
                <button onclick="updateOrderStatus(${orderId}, 'DELIVERED')" class="btn-secondary-sm">
                    Mark Delivered
                </button>
            `;

    case "DELIVERED":
      return `
                <button onclick="updateOrderStatus(${orderId}, 'COMPLETED')" class="btn-primary-sm">
                    Complete
                </button>
            `;

    case "COMPLETED":
      return `<span class="text-green-600 font-semibold">✔ Done</span>`;

    case "CANCELLED":
      return `<span class="text-red-600 font-semibold">✖ Cancelled</span>`;

    default:
      return "";
  }
}

// ─────────────────────────────────────────
//   UPDATE ORDER STATUS (PUT API)
//   PUT /api/orders/{id}?status=VALUE
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
    updateOrderRow(updatedOrder);
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
