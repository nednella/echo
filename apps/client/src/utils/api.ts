import { toast } from "sonner"

import { ApiException, type Middleware, createClient } from "@/libs/api"

const BASE_URL = import.meta.env.VITE_API_BASE_URL

// TODO: validate .env variables in one place
if (!BASE_URL) {
    throw new Error("API base URL missing from .env file!")
}

const authMiddleware: Middleware = {
    onRequest: async ({ request }) => {
        const token = await globalThis.Clerk?.session?.getToken()

        if (token) {
            request.headers.append("Authorization", `Bearer ${token}`)
        }
    }
}

const errorMiddleware: Middleware = {
    onResponse: async ({ response }) => {
        if (response.ok) return response

        const error = await response.json()
        throw new ApiException(error, response) // https://openapi-ts.dev/openapi-fetch/middleware-auth#throwing
    },
    onError: () => {
        toast.error("A network error occurred", { description: "Please try again later" })
    }
}

function createClientWithMiddleware() {
    const client = createClient(BASE_URL)
    client.use(authMiddleware)
    client.use(errorMiddleware)
    return client
}

export const client = createClientWithMiddleware()
