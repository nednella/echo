import { useEffect } from "react"

import { createFileRoute } from "@tanstack/react-router"

import { ComingSoon } from "@/components/coming-soon"
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
        <div className="flex h-full items-center justify-center">
            <ComingSoon />
        </div>
    )
}
