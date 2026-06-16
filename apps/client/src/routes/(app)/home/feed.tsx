import { createFileRoute } from "@tanstack/react-router"

import { feedInfiniteQueryOptions } from "@/features/feed/api/options"
import { Feed } from "@/features/feed/components/feed"

export const Route = createFileRoute("/(app)/home/feed")({
    loader: ({ context: { queryClient } }) => queryClient.ensureInfiniteQueryData(feedInfiniteQueryOptions("homepage")),
    component: () => <Feed feed="homepage" />
})
