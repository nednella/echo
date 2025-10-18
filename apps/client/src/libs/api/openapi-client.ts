import createClient from "openapi-fetch"

import { authMiddleware, errorMiddleware } from "./middleware"
import type { components, paths } from "./v1"

function createClientWithMiddleware() {
    const client = createClient<paths>({ baseUrl: import.meta.env.VITE_API_BASE_URL })
    client.use(authMiddleware)
    client.use(errorMiddleware)
    return client
}

export const client = createClientWithMiddleware()
export type schemas = components["schemas"]
