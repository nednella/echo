import { useState } from "react"

import { useAuth } from "@clerk/clerk-react"
import { RouterProvider } from "@tanstack/react-router"

import { router } from "@/common/router"

import { LoadingPage } from "./loading"
import { AppProvider } from "./providers"

export function App() {
    return (
        <AppProvider>
            <EntryPoint />
        </AppProvider>
    )
}

function EntryPoint() {
    const [isReady, setIsReady] = useState(false)
    const auth = useAuth()

    return isReady ? (
        <RouterProvider
            router={router}
            context={{ auth }}
        />
    ) : (
        <LoadingPage
            isReady={auth.isLoaded} // base on Clerk load status
            minimumLoadingTimeMs={2000}
            onAnimationComplete={() => setIsReady(true)}
        />
    )
}
