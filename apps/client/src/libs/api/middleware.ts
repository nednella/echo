import type { Middleware } from "openapi-fetch"
import { toast } from "sonner"

import { ApiException } from "./exception"

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

export { authMiddleware, errorMiddleware }
