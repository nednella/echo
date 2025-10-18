import { createRouter } from "@tanstack/react-router"

import { queryClient } from "@/libs/api/query-client"
import { routeTree } from "@/routeTree.gen"

declare module "@tanstack/react-router" {
    interface Register {
        router: typeof router
    }
}

export const router = createRouter({
    routeTree,
    notFoundMode: "root",
    defaultPreload: "intent",
    scrollRestoration: true, //  https://tanstack.com/router/latest/docs/framework/react/examples/scroll-restoration
    context: {
        auth: undefined!,
        queryClient: queryClient
    }
})
