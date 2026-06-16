import { createFileRoute } from "@tanstack/react-router"

import { feedInfiniteQueryOptions } from "@/features/feed/api/options"
import { Feed } from "@/features/feed/components/feed"

export const Route = createFileRoute("/(app)/home/discover")({
    loader: ({ context: { queryClient } }) => queryClient.ensureInfiniteQueryData(feedInfiniteQueryOptions("discover")),
    component: () => <Feed feed="discover" />
})
