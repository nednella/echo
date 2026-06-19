import { mutationOptions, queryOptions } from "@tanstack/react-query"

import { client, type schemas } from "@/libs/api/openapi-client"
import { PAGE_SIZE, pagedInfiniteQueryOptions } from "@/utils/pagination"

export type ProfileFeed = "posts" | "replies" | "likes" | "mentions"

export const profileQueryOptions = (username: string) =>
    queryOptions({
        queryKey: ["profile", username],
        queryFn: async () => {
            const { data } = await client.GET("/v1/profile/{username}", {
                params: { path: { username } }
            })
            return data!
        }
    })

export const currentProfileQueryOptions = () =>
    queryOptions({
        queryKey: ["profile", "me"],
        queryFn: async () => {
            const { data } = await client.GET("/v1/profile/me")
            return data!
        }
    })

export const profileFeedInfiniteQueryOptions = (id: string, feed: ProfileFeed) =>
    pagedInfiniteQueryOptions<schemas["Post"]>(["feed", "profile", id, feed], async (offset) => {
        const parameters = {
            path: { id },
            query: {
                offset: String(offset),
                limit: String(PAGE_SIZE)
            }
        }
        switch (feed) {
            case "posts": {
                const { data } = await client.GET("/v1/feed/profile/{id}/posts", { params: parameters })
                return data!
            }
            case "replies": {
                const { data } = await client.GET("/v1/feed/profile/{id}/replies", { params: parameters })
                return data!
            }
            case "likes": {
                const { data } = await client.GET("/v1/feed/profile/{id}/likes", { params: parameters })
                return data!
            }
            case "mentions": {
                const { data } = await client.GET("/v1/feed/profile/{id}/mentions", { params: parameters })
                return data!
            }
        }
    })

export const updateProfileMutationOptions = () =>
    mutationOptions({
        mutationFn: async (body: schemas["UpdateProfileRequest"]) => {
            const { data } = await client.PUT("/v1/profile/me", { body })
            return data
        }
    })

export const toggleFollowMutationOptions = () =>
    mutationOptions({
        mutationFn: ({ id, following }: { id: string; following: boolean }) =>
            following
                ? client.DELETE("/v1/profile/{id}/follow", { params: { path: { id } } })
                : client.POST("/v1/profile/{id}/follow", { params: { path: { id } } })
    })
