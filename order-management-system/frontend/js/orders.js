async function load() {
    const status = document.getElementById("filterStatus").value;
    const path = status ? `/api/orders?status=${status}` : "/api/orders";
    const orders = await api.get(path);
    const tbody = document.getElementById("tbody");

    if (!orders.length) {
        tbody.innerHTML = `<tr><td colspan="5" class="empty">Nenhum pedido encontrado.</td></tr>`;
        return;
    }
    tbody.innerHTML = orders.map(o => `
        <tr>
            <td>${o.customerName}</td>
            <td>${new Date(o.createdAt).toLocaleDateString("pt-BR")}</td>
            <td>R$ ${Number(o.totalAmount).toFixed(2)}</td>
            <td><span class="badge badge-${o.status}">${o.status}</span></td>
            <td>
                <a class="btn btn-sm btn-primary" href="order-detail.html?id=${o.id}">Detalhes</a>
            </td>
        </tr>`).join("");
}

async function openModal() {
    const customers = await api.get("/api/customers");
    const sel = document.getElementById("customerId");
    sel.innerHTML = customers.map(c => `<option value="${c.id}">${c.name}</option>`).join("");
    document.getElementById("modal").classList.add("open");
}

function closeModal() {
    document.getElementById("modal").classList.remove("open");
}

async function createOrder() {
    const customerId = document.getElementById("customerId").value;
    try {
        const order = await api.post("/api/orders", { customerId });
        toast("Pedido criado!");
        closeModal();
        window.location.href = `order-detail.html?id=${order.id}`;
    } catch (e) {
        toast(e.message, true);
    }
}

function toast(msg, err) {
    const t = document.getElementById("toast");
    t.textContent = msg;
    t.style.background = err ? "#c53030" : "#2d3748";
    t.classList.add("show");
    setTimeout(() => t.classList.remove("show"), 2800);
}

load();
