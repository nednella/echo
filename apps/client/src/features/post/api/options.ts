import { mutationOptions } from "@tanstack/react-query"

import { client, type schemas } from "@/libs/api/openapi-client"

export const createPostMutationOptions = () =>
    mutationOptions({
        mutationFn: async (body: schemas["CreatePostRequest"]) => {
            const { data } = await client.POST("/v1/post", { body })
            return data
        }
    })
