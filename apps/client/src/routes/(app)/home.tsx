import { useEffect } from "react"

import { UserButton } from "@clerk/clerk-react"
import { createFileRoute } from "@tanstack/react-router"

import { client } from "@/common/api/client"

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
        <div className="flex h-full items-center justify-center gap-6">
            Hello "/(protected)/home"!
            <UserButton />
        </div>
    )
}
