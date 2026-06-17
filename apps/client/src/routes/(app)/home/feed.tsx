import { createFileRoute } from "@tanstack/react-router"

import { feedInfiniteQueryOptions } from "@/features/feed/api/options"
import { PostList } from "@/features/post/components/post-list"

export const Route = createFileRoute("/(app)/home/feed")({
    loader: ({ context: { queryClient } }) => queryClient.ensureInfiniteQueryData(feedInfiniteQueryOptions("homepage")),
    component: () => <PostList options={feedInfiniteQueryOptions("homepage")} />
})
