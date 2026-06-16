import { createFileRoute } from "@tanstack/react-router"

import { ComingSoon } from "@/components/coming-soon"
import { AppPage } from "@/components/page/app-page"

export const Route = createFileRoute("/(app)/profile/$username")({
    component: ProfilePage
})

function ProfilePage() {
    const { username } = Route.useParams()

    return (
        <AppPage>
            <div className="flex flex-1 flex-col items-center justify-center gap-3">
                <p className="text-xl font-semibold">@{username}</p>
                <ComingSoon />
            </div>
        </AppPage>
    )
}
