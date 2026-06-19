import { createFileRoute } from "@tanstack/react-router"

import { ComingSoon } from "@/components/coming-soon"
import { AppRoute } from "@/components/page/app-route"

export const Route = createFileRoute("/(app)/post/$id")({
    component: () => (
        <AppRoute center>
            <ComingSoon />
        </AppRoute>
    )
})
