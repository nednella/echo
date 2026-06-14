import { createFileRoute } from "@tanstack/react-router"

import { ComingSoon } from "@/components/coming-soon"

export const Route = createFileRoute("/(app)/home")({
    component: HomePage
})

function HomePage() {
    return (
        <div className="mx-auto flex h-full max-w-xl items-center justify-center">
            <ComingSoon />
        </div>
    )
}
