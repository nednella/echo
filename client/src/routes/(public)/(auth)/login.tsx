import { SignIn } from "@clerk/clerk-react"
import { createFileRoute } from "@tanstack/react-router"

export const Route = createFileRoute("/(public)/(auth)/login")({
    component: RouteComponent
})

function RouteComponent() {
    return (
        <div className="flex h-full items-center justify-center">
            <SignIn />
        </div>
    )
}
