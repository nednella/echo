import { infiniteQueryOptions } from "@tanstack/react-query"

import { client } from "@/libs/api/openapi-client"

const PAGE_SIZE = 20

export type FeedType = "homepage" | "discover"

export function feedInfiniteQueryOptions(feed: FeedType) {
    return infiniteQueryOptions({
        queryKey: ["feed", feed],
        queryFn: async ({ pageParam }) => {
            const query = { offset: String(pageParam), limit: String(PAGE_SIZE) }
            const { data } =
                feed === "homepage"
                    ? await client.GET("/v1/feed/homepage", { params: { query } })
                    : await client.GET("/v1/feed/discover", { params: { query } })
            return data!
        },
        initialPageParam: 0,
        getNextPageParam: (last) => {
            const next = last.offset + last.limit
            return next < last.total ? next : undefined
        }
    })
}
