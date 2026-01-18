import http from "k6/http";
import { check, sleep } from "k6";
import { Trend, Rate } from "k6/metrics";

export const options = {
    scenarios: {
        load_1m_ramp_hold_ramp: {
            executor: "ramping-vus",
            startVUs: 10,
            stages: [
                { duration: "20s", target: 50 }, // 10 -> 50
                { duration: "20s", target: 50 }, // hold 50
                { duration: "20s", target: 10 }, // 50 -> 10
            ],
            gracefulRampDown: "5s",
            gracefulStop: "5s",
        },
    },
    thresholds: {
        http_req_failed: ["rate<0.01"],
        http_req_duration: ["p(95)<800"],
    },
};

const latency = new Trend("latency", true);
const okRate = new Rate("ok_rate");

const BASE_URL = __ENV.BASE_URL || "http://localhost:8081";
const PRODUCT_NAME = __ENV.PRODUCT_NAME || "노트";

function urlWithQuery(path, productName) {
    const q = encodeURIComponent(productName);
    return `${BASE_URL}${path}?productName=${q}`;
}

export default function () {
    const url = urlWithQuery("/api/products/es", PRODUCT_NAME);

    const res = http.get(url, { tags: { endpoint: "es" } });

    const ok = check(
        res,
        { "status is 200": (r) => r.status === 200 },
        { endpoint: "es" }
    );

    latency.add(res.timings.duration, { endpoint: "es" });
    okRate.add(ok, { endpoint: "es" });

    sleep(0.1);
}