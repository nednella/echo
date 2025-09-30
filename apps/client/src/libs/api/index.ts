import createClient from "openapi-fetch"

import type { components, paths } from "./v1"

const BASE_URL = import.meta.env.VITE_API_BASE_URL

// TODO: validate .env variables in one place
if (!BASE_URL) {
    throw new Error("API base URL missing from .env file!")
}

export const client = createClient<paths>({ baseUrl: BASE_URL })

export type { paths, operations } from "./v1"
export type schemas = components["schemas"]
