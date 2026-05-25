async function load() {
    const stats = await api.get("/api/dashboard/stats");

    document.getElementById("totalOrders").textContent = stats.totalOrders;
    document.getElementById("totalCustomers").textContent = stats.totalCustomers;
    document.getElementById("avgAmount").textContent =
        "R$ " + Number(stats.averageOrderAmount).toFixed(2);
    document.getElementById("delivered").textContent =
        stats.ordersByStatus["DELIVERED"] ?? 0;

    const tbody = document.getElementById("statusTable");
    tbody.innerHTML = Object.entries(stats.ordersByStatus).map(([s, n]) =>
        `<tr><td><span class="badge badge-${s}">${s}</span></td><td>${n}</td></tr>`
    ).join("");
}

load();
