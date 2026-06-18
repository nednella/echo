import { createFileRoute } from "@tanstack/react-router"

import { feedInfiniteQueryOptions } from "@/features/feed/api/options"
import { PostList } from "@/features/post/components/post-list"

export const Route = createFileRoute("/(app)/home/discover")({
    loader: ({ context: { queryClient } }) => queryClient.ensureInfiniteQueryData(feedInfiniteQueryOptions("discover")),
    component: () => <PostList options={feedInfiniteQueryOptions("discover")} />
})
