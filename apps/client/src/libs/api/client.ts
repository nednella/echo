import createClient from "openapi-fetch"

import type { paths } from "./api-spec"

const BASE_URL = import.meta.env.VITE_API_BASE_URL

if (!BASE_URL) {
    throw new Error("API base URL missing from .env file!")
}

export const client = createClient<paths>({ baseUrl: BASE_URL })
