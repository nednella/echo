import { AuthenticateWithRedirectCallback } from "@clerk/clerk-react"
import { createFileRoute } from "@tanstack/react-router"

export const Route = createFileRoute("/(public)/(auth)/sso-callback")({
    component: RouteComponent
})

function RouteComponent() {
    return <AuthenticateWithRedirectCallback />
}
