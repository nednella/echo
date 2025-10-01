import { useState } from "react"

import { useAuth } from "@clerk/clerk-react"
import { RouterProvider } from "@tanstack/react-router"

import { AppProvider } from "@/providers"

import { LoadingPage } from "./loading"
import { router } from "./router"

function InnerApp() {
    const [isReady, setIsReady] = useState(false)
    const auth = useAuth()

    return isReady ? (
        <RouterProvider
            router={router}
            context={{ auth }}
        />
    ) : (
        <LoadingPage
            isReady={auth.isLoaded} // based on Clerk loading status
            minimumLoadingTime={2000}
            onAnimationComplete={() => setIsReady(true)} // render app after animation complete
        />
    )
}

export function App() {
    return (
        <AppProvider>
            <InnerApp />
        </AppProvider>
    )
}
