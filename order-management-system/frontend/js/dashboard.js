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

    loadLambdaReport();
}

async function loadLambdaReport() {
    const el = document.getElementById("lambdaReport");
    try {
        const relatorio = await api.get("/report");
        el.innerHTML = `
            <div class="lambda-meta">
                <span>Fonte: <strong>${relatorio.fonte}</strong></span>
                <span>Função: <strong>${relatorio.funcao}</strong></span>
                <span>Versão: <strong>${relatorio.versaoRelatorio}</strong></span>
                <span>Gerado em: <strong>${new Date(relatorio.geradoEm).toLocaleString("pt-BR")}</strong></span>
            </div>
            <pre class="lambda-json">${JSON.stringify(relatorio.dados, null, 2)}</pre>
        `;
    } catch (_) {
        el.innerHTML = `<p class="lambda-error">Relatório Lambda disponível apenas em produção (API Gateway → Lambda → ECS).</p>`;
    }
}

load();
