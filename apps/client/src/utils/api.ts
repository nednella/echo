import { toast } from "sonner"

import { type Middleware, createClient } from "@/libs/api"

const BASE_URL = import.meta.env.VITE_API_BASE_URL

// TODO: validate .env variables in one place
if (!BASE_URL) {
    throw new Error("API base URL missing from .env file!")
}

const errorMiddleware: Middleware = {
    onResponse: () => {
        console.log("onResponse")
    },
    onError: () => {
        toast.error("A network error occurred", { description: "Please try again later" })
    }
}

function createClientWithMiddleware() {
    const client = createClient(BASE_URL)
    client.use(errorMiddleware)
    return client
}

export const client = createClientWithMiddleware()
