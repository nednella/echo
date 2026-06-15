import { createFileRoute } from "@tanstack/react-router"

import { ComingSoon } from "@/components/coming-soon"
import { AppPage } from "@/components/page/app-page"
import { FeedContainer } from "@/features/feed/components/feed-container"
import { PostComposerButton, PostComposerPrompt } from "@/features/post/components/post-composer-triggers"

export const Route = createFileRoute("/(app)/home")({
    component: HomePage
})

function HomePage() {
    return (
        <AppPage>
            <FeedContainer>
                <PostComposerPrompt />
                <div className="flex flex-1 items-center justify-center">
                    <ComingSoon />
                </div>
            </FeedContainer>

            <PostComposerButton />
        </AppPage>
    )
}
