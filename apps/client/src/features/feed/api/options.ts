import { client, type schemas } from "@/libs/api/openapi-client"
import { PAGE_SIZE, pagedInfiniteQueryOptions } from "@/utils/pagination"

export type FeedType = "homepage" | "discover"

export const feedInfiniteQueryOptions = (feed: FeedType) =>
    pagedInfiniteQueryOptions<schemas["Post"]>(["feed", feed], async (offset) => {
        const query = {
            offset: String(offset),
            limit: String(PAGE_SIZE)
        }
        const { data } =
            feed === "homepage"
                ? await client.GET("/v1/feed/homepage", { params: { query } })
                : await client.GET("/v1/feed/discover", { params: { query } })
        return data!
    })
