import { Outlet, createFileRoute } from "@tanstack/react-router"

import { AppPage } from "@/components/page/app-page"
import { FeedContainer } from "@/features/feed/components/feed-container"
import { FeedTabs } from "@/features/feed/components/feed-tabs"
import { PostComposerButton, PostComposerPrompt } from "@/features/post/components/post-composer-triggers"

export const Route = createFileRoute("/(app)/home")({
    component: HomeLayout
})

function HomeLayout() {
    return (
        <AppPage>
            <FeedContainer>
                <FeedTabs />
                <PostComposerPrompt />
                <Outlet />
            </FeedContainer>
            <PostComposerButton />
        </AppPage>
    )
}
