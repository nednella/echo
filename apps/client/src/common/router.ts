import { createRouter } from "@tanstack/react-router"

import { routeTree } from "@/routeTree.gen"

import { queryClient } from "./api/query-client"

declare module "@tanstack/react-router" {
    interface Register {
        router: typeof router
    }
}

export const router = createRouter({
    routeTree,
    defaultPreload: "intent",
    scrollRestoration: true, //  https://tanstack.com/router/latest/docs/framework/react/examples/scroll-restoration
    context: {
        auth: undefined!,
        queryClient: queryClient
    }
})
