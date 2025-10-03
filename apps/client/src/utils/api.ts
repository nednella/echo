import { QueryClient } from "@tanstack/react-query"

import { createClient } from "@/libs/api"

const BASE_URL = import.meta.env.VITE_API_BASE_URL

// TODO: validate .env variables in one place
if (!BASE_URL) {
    throw new Error("API base URL missing from .env file!")
}

export const client = createClient(BASE_URL)

export const queryClient = new QueryClient({
    defaultOptions: {}
})
