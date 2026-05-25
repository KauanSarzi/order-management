const API_BASE_URL = window.location.hostname === "localhost"
    ? "http://localhost:8080"
    : "https://<YOUR-API-GATEWAY-ID>.execute-api.<REGION>.amazonaws.com/prod";

async function request(method, path, body) {
    const opts = {
        method,
        headers: { "Content-Type": "application/json" },
    };
    if (body !== undefined) opts.body = JSON.stringify(body);

    const res = await fetch(`${API_BASE_URL}${path}`, opts);
    if (res.status === 204) return null;
    const data = await res.json();
    if (!res.ok) throw new Error(data.message || `HTTP ${res.status}`);
    return data;
}

const api = {
    get: (path) => request("GET", path),
    post: (path, body) => request("POST", path, body),
    put: (path, body) => request("PUT", path, body),
    del: (path) => request("DELETE", path),
};
