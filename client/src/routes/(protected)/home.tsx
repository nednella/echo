import { UserButton } from "@clerk/clerk-react"
import { createFileRoute } from "@tanstack/react-router"

export const Route = createFileRoute("/(protected)/home")({
    component: RouteComponent
})

function RouteComponent() {
    return (
        <div className="flex h-full items-center justify-center gap-6">
            Hello "/(protected)/home"!
            <UserButton />
        </div>
    )
}
