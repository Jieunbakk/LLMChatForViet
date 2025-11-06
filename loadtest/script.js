// loadtest/script.js
import http from 'k6/http';
import { sleep, check } from 'k6';

export const options = {
    vus: Number(__ENV.VUS || 50),
    duration: __ENV.DURATION || '10m',
    thresholds: {
        http_req_duration: ['p(95)<' + (__ENV.P95 || 500)]
    }
};

const BASE_URL = __ENV.URL || 'http://localhost:8080';
const TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJjYXRlZ29yeSI6ImFjY2VzcyIsInVzZXJJZCI6ImppZXVuIiwicGFzc3dvcmQiOiJST0xFX1VTRVIiLCJpYXQiOjE3NTkwNTcwNzEsImV4cCI6MTc1OTA5MzA3MX0.Sfi61NkkjU9fMqFVm4ThJj8TpTWZ8Q62MVIVb6q2ZOA";

export default function () {
    const payload = JSON.stringify({ conversationId: 31, input: '안녕' });
    const params = {
        headers: {
            'Content-Type': 'application/json',
            ...(TOKEN ? { Authorization: TOKEN } : {})
        },
        body:{
            'input':"안녕",
            'conversationId':31
        }
    };
    const res = http.post(`${BASE_URL}/v1/ai/correction`, payload, params);
    check(res, { 'status is 200': r => r.status === 200 });
    sleep(Number(__ENV.SLEEP || 0.1));
}
