import http from 'k6/http'
import { check } from 'k6'

export default function () {
    let res = http.get('http://localhost:8112/jooq/users/min-json?page=5&size=20')

    check(res, { 'success response': (r) => r.status === 200 })
}