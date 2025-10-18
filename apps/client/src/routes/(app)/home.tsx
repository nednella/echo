import { useEffect } from "react"

import { UserButton } from "@clerk/clerk-react"
import { createFileRoute } from "@tanstack/react-router"

import { client } from "@/libs/api/openapi-client"

export const Route = createFileRoute("/(app)/home")({
    component: HomePage
})

function HomePage() {
    useEffect(() => {
        const fetch = async () => {
            const { data } = await client.GET("/v1/profile/me")
            console.log(data)
        }

        fetch()
    }, [])

    return (
        <>
            <header className="h-16 text-center">Optional Content Header</header>
            <div className="flex h-full items-center justify-center gap-2">
                Hello "/(app)/home"! <UserButton />
            </div>
        </>
    )
}
