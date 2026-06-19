import { mutationOptions } from "@tanstack/react-query"

import { client, type schemas } from "@/libs/api/openapi-client"

export const createPostMutationOptions = () =>
    mutationOptions({
        mutationFn: async (body: schemas["CreatePostRequest"]) => {
            const { data } = await client.POST("/v1/post", { body })
            return data
        }
    })

export const toggleLikeMutationOptions = () =>
    mutationOptions({
        mutationFn: ({ id, liked }: { id: string; liked: boolean }) =>
            liked
                ? client.DELETE("/v1/post/{id}/like", { params: { path: { id } } })
                : client.POST("/v1/post/{id}/like", { params: { path: { id } } })
    })

export const deletePostMutationOptions = () =>
    mutationOptions({
        mutationFn: ({ id }: { id: string }) => client.DELETE("/v1/post/{id}", { params: { path: { id } } })
    })
