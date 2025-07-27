import AuthProvider from "../providers/AuthProvider"
import { routeTree } from "../routeTree.gen"
import { useAuth } from "@clerk/clerk-react"
import { RouterProvider, createRouter } from "@tanstack/react-router"

import AppLoading from "./AppLoading"

const router = createRouter({
    routeTree,
    defaultPreload: false, // TODO: use "intent"
    scrollRestoration: true, //  https://tanstack.com/router/latest/docs/framework/react/examples/scroll-restoration
    context: { auth: undefined! }
})

declare module "@tanstack/react-router" {
    interface Register {
        router: typeof router
    }
}

export default function Wrapper() {
    return (
        <AuthProvider>
            <App />
        </AuthProvider>
    )
}

function App() {
    const auth = useAuth()

    if (!auth.isLoaded) return <AppLoading />

    return (
        <RouterProvider
            router={router}
            context={{ auth }}
        />
    )
}
