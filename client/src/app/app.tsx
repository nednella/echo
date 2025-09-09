import { useState } from "react"

import { useAuth } from "@clerk/clerk-react"
import { RouterProvider } from "@tanstack/react-router"

import LoadingPage from "./loading"
import Provider from "./provider"
import { router } from "./router"

function InnerApp() {
    const [isReady, setIsReady] = useState(false)
    const auth = useAuth()

    return !isReady ? (
        <LoadingPage
            isReady={auth.isLoaded} // based on Clerk loading status
            minimumLoadingTime={2000}
            onAnimationComplete={() => setIsReady(true)} // render app after animation complete
        />
    ) : (
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
