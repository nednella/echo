import { createFileRoute, redirect } from "@tanstack/react-router"

export const Route = createFileRoute("/(app)/home/")({
    beforeLoad: () => {
        throw redirect({ to: "/home/feed" })
    }
})
