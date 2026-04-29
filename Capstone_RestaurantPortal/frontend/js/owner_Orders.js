/**
 * ============================================
 *   OWNER ORDERS PAGE
 * ============================================
 */

// ─────────────────────────────────────────
//   PAGE INIT
// ─────────────────────────────────────────
document.addEventListener('DOMContentLoaded', () => {
    requireAuth();   // from utils.js
    loadOrders();
});


// ─────────────────────────────────────────
//   LOAD ORDERS (GET /api/orders/owner)
// ─────────────────────────────────────────
async function loadOrders() {
    try {
        const res = await apiFetch('/api/orders/owner');

        if (!res.ok) throw new Error("Failed to fetch orders");

        const orders = await res.json();

        renderOrders(orders);

    } catch (err) {
        console.error(err);
        showToast('error', '❌', 'Failed to load orders');
    }
}


// ─────────────────────────────────────────
//   RENDER ORDERS TABLE
// ─────────────────────────────────────────
function renderOrders(orders) {
    const tbody = document.getElementById('ordersTbody');
    tbody.innerHTML = '';

    if (!orders || orders.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="5" class="text-center py-6 text-ash">
                    No orders found
                </td>
            </tr>`;
        return;
    }

    orders.forEach(order => {
        const tr = document.createElement('tr');

        tr.innerHTML = `
            <td class="font-semibold">#${order.id}</td>

            <td>${order.customerName || 'User'}</td>

            <td class="font-semibold text-emerald">
                ₹${Number(order.totalAmount || 0).toFixed(2)}
            </td>

            <td>
                <span class="status-badge status-${order.status}">
                    ${getStatusIcon(order.status)} ${order.status}
                </span>
            </td>

            <td>
                ${renderActionButtons(order)}
            </td>
        `;

        tbody.appendChild(tr);
    });
}


// ─────────────────────────────────────────
//   STATUS ICONS
// ─────────────────────────────────────────
function getStatusIcon(status) {
    switch (status) {
        case "PLACED": return "🆕";
        case "PENDING": return "⏳";
        case "DELIVERED": return "🚚";
        case "COMPLETED": return "✅";
        case "CANCELLED": return "❌";
        default: return "";
    }
}


// ─────────────────────────────────────────
//   ACTION BUTTONS BASED ON STATUS
// ─────────────────────────────────────────
function renderActionButtons(order) {
    switch (order.status) {
        case "PLACED":
            return `
                <button onclick="updateOrderStatus(${order.id}, 'PENDING')" class="btn-secondary-sm">
                    Accept
                </button>
                <button onclick="updateOrderStatus(${order.id}, 'CANCELLED')" class="btn-danger-sm">
                    Cancel
                </button>
            `;

        case "PENDING":
            return `
                <button onclick="updateOrderStatus(${order.id}, 'DELIVERED')" class="btn-secondary-sm">
                    Mark Delivered
                </button>
            `;

        case "DELIVERED":
            return `
                <button onclick="updateOrderStatus(${order.id}, 'COMPLETED')" class="btn-primary-sm">
                    Complete
                </button>
            `;

        case "COMPLETED":
            return `<span class="text-green-600 font-semibold">✔ Done</span>`;

        case "CANCELLED":
            return `<span class="text-red-600 font-semibold">✖ Cancelled</span>`;

        default:
            return '';
    }
}


// ─────────────────────────────────────────
//   UPDATE ORDER STATUS (PUT API)
//   PUT /api/orders/{id}?status=VALUE
// ─────────────────────────────────────────
async function updateOrderStatus(orderId, status) {
    try {
        const res = await apiFetch(
            `/api/orders/${orderId}?status=${status}`,
            { method: 'PUT' }
        );

        if (!res.ok) {
            const err = await res.text();
            throw new Error(err || "Update failed");
        }

        showToast('success', '✅', `Order ${status}`);

        loadOrders(); // refresh table

    } catch (err) {
        showToast('error', '❌', err.message || 'Failed to update order');
    }
}


// ─────────────────────────────────────────
//   BACK BUTTON
// ─────────────────────────────────────────
function goBack() {
    window.location.href = 'owner-dashboard.html';
}