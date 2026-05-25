const orderId = new URLSearchParams(location.search).get("id");

async function load() {
    const order = await api.get(`/api/orders/${orderId}`);

    document.getElementById("orderTitle").textContent = `Pedido #${order.id.slice(0, 8).toUpperCase()}`;
    document.getElementById("customerInfo").textContent =
        `${order.customerName} — R$ ${Number(order.totalAmount).toFixed(2)}`;

    document.getElementById("newStatus").value = order.status;

    const badge = document.getElementById("currentStatus");
    badge.textContent = order.status;
    badge.className = `badge badge-${order.status}`;
    document.getElementById("updatedAt").textContent =
        `Última atualização: ${new Date(order.updatedAt).toLocaleString("pt-BR")}`;

    renderItems(order.items);
}

function renderItems(items) {
    const tbody = document.getElementById("itemsBody");
    if (!items.length) {
        tbody.innerHTML = `<tr><td colspan="5" class="empty">Nenhum item adicionado.</td></tr>`;
        return;
    }
    tbody.innerHTML = items.map(i => `
        <tr>
            <td>${i.productName}</td>
            <td>${i.quantity}</td>
            <td>R$ ${Number(i.unitPrice).toFixed(2)}</td>
            <td>R$ ${Number(i.subtotal).toFixed(2)}</td>
            <td><button class="btn btn-sm btn-danger" onclick="removeItem('${i.id}')">Remover</button></td>
        </tr>`).join("");
}

async function updateStatus() {
    const status = document.getElementById("newStatus").value;
    try {
        await api.put(`/api/orders/${orderId}/status`, { status });
        toast("Status atualizado!");
        load();
    } catch (e) {
        toast(e.message, true);
    }
}

async function addItem() {
    const body = {
        productName: document.getElementById("productName").value.trim(),
        quantity: parseInt(document.getElementById("quantity").value),
        unitPrice: parseFloat(document.getElementById("unitPrice").value),
    };
    try {
        await api.post(`/api/orders/${orderId}/items`, body);
        toast("Item adicionado!");
        closeItemModal();
        load();
    } catch (e) {
        toast(e.message, true);
    }
}

async function removeItem(itemId) {
    if (!confirm("Remover este item?")) return;
    try {
        await api.del(`/api/orders/${orderId}/items/${itemId}`);
        toast("Item removido.");
        load();
    } catch (e) {
        toast(e.message, true);
    }
}

function openItemModal() {
    document.getElementById("productName").value = "";
    document.getElementById("quantity").value = "1";
    document.getElementById("unitPrice").value = "";
    document.getElementById("itemModal").classList.add("open");
}

function closeItemModal() {
    document.getElementById("itemModal").classList.remove("open");
}

function toast(msg, err) {
    const t = document.getElementById("toast");
    t.textContent = msg;
    t.style.background = err ? "#c53030" : "#2d3748";
    t.classList.add("show");
    setTimeout(() => t.classList.remove("show"), 2800);
}

load();
