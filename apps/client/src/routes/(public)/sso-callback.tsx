import { AuthenticateWithRedirectCallback } from "@clerk/clerk-react"
import { createFileRoute } from "@tanstack/react-router"

export const Route = createFileRoute("/(public)/sso-callback")({
    component: () => <AuthenticateWithRedirectCallback />
})
