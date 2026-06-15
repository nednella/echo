import { createFileRoute } from "@tanstack/react-router"

import { ComingSoon } from "@/components/coming-soon"
import { AppPage } from "@/components/page/app-page"
import { PostComposerButton, PostComposerPrompt } from "@/features/post/components/post-composer-triggers"

export const Route = createFileRoute("/(app)/home")({
    component: HomePage
})

function HomePage() {
    return (
        <AppPage>
            <div className="hidden border-b px-4 py-4 lg:block">
                <PostComposerPrompt />
            </div>
            <div className="flex flex-1 items-center justify-center">
                <ComingSoon />
            </div>

            <PostComposerButton />
        </AppPage>
    )
}
