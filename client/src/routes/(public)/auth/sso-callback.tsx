import { createFileRoute } from "@tanstack/react-router"

export const Route = createFileRoute("/(public)/auth/sso-callback")({
    component: RouteComponent
})

function RouteComponent() {
    return <div>Hello "/(public)/auth/sso-callback"!</div>
}
