import { useEffect } from "react"

import { UserButton, useAuth } from "@clerk/clerk-react"
import { createFileRoute } from "@tanstack/react-router"

import { client } from "@/libs/api/client"

export const Route = createFileRoute("/(protected)/home")({
    component: HomePage
})

function HomePage() {
    const { getToken } = useAuth()

    useEffect(() => {
        const fetch = async () => {
            const token = await getToken()

            const response = await client.GET("/v1/profile/me", {
                headers: { Authorization: "Bearer " + token },
                credentials: "include"
            })
            console.log(response)
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
