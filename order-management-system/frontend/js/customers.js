let editingId = null;

async function load() {
    const customers = await api.get("/api/customers");
    const tbody = document.getElementById("tbody");
    if (!customers.length) {
        tbody.innerHTML = `<tr><td colspan="4" class="empty">Nenhum cliente cadastrado.</td></tr>`;
        return;
    }
    tbody.innerHTML = customers.map(c => `
        <tr>
            <td>${c.name}</td>
            <td>${c.email}</td>
            <td>${c.phone ?? "–"}</td>
            <td>
                <button class="btn btn-sm btn-primary" onclick="openModal('${c.id}','${esc(c.name)}','${esc(c.email)}','${esc(c.phone??"")}','${esc(c.address??"")}')">Editar</button>
                <button class="btn btn-sm btn-danger" onclick="remove('${c.id}')">Excluir</button>
            </td>
        </tr>`).join("");
}

function openModal(id, name, email, phone, address) {
    editingId = id ?? null;
    document.getElementById("modalTitle").textContent = id ? "Editar Cliente" : "Novo Cliente";
    document.getElementById("name").value = name ?? "";
    document.getElementById("email").value = email ?? "";
    document.getElementById("phone").value = phone ?? "";
    document.getElementById("address").value = address ?? "";
    document.getElementById("modal").classList.add("open");
}

function closeModal() {
    document.getElementById("modal").classList.remove("open");
    editingId = null;
}

async function save() {
    const body = {
        name: document.getElementById("name").value.trim(),
        email: document.getElementById("email").value.trim(),
        phone: document.getElementById("phone").value.trim() || null,
        address: document.getElementById("address").value.trim() || null,
    };
    try {
        if (editingId) {
            await api.put(`/api/customers/${editingId}`, body);
            toast("Cliente atualizado!");
        } else {
            await api.post("/api/customers", body);
            toast("Cliente criado!");
        }
        closeModal();
        load();
    } catch (e) {
        toast(e.message, true);
    }
}

async function remove(id) {
    if (!confirm("Excluir este cliente?")) return;
    try {
        await api.del(`/api/customers/${id}`);
        toast("Cliente removido.");
        load();
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

function esc(s) { return s.replace(/'/g, "\\'"); }

load();
