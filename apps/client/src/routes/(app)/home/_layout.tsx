import { Outlet, createFileRoute } from "@tanstack/react-router"

import { AppRoute } from "@/components/page/app-route"
import { FeedContainer } from "@/features/feed/components/feed-container"
import { FeedTabs } from "@/features/feed/components/feed-tabs"
import { PostComposerButton, PostComposerPrompt } from "@/features/post/components/post-composer-triggers"

export const Route = createFileRoute("/(app)/home")({
    component: HomeLayout
})

function HomeLayout() {
    return (
        <AppRoute>
            <FeedContainer>
                <FeedTabs />
                <PostComposerPrompt />
                <Outlet />
            </FeedContainer>
            <PostComposerButton />
        </AppRoute>
    )
}
