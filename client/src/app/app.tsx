import { useAuth } from "@clerk/clerk-react"
import { RouterProvider } from "@tanstack/react-router"

import Provider from "./provider"
import { router } from "./router"

function InnerApp() {
    const auth = useAuth()

    return (
        <RouterProvider
            router={router}
            context={{ auth }}
        />
    )
}

export default function App() {
    return (
        <Provider>
            <InnerApp />
        </Provider>
    )
}
