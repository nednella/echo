import type { ReactNode } from "react"

import { PostAvatar } from "@/features/post/components/post-avatar"
import { PostMetadata } from "@/features/post/components/post-metadata"
import { PostText } from "@/features/post/components/post-text"
import type { schemas } from "@/libs/api/openapi-client"

type PostPreviewProps = Readonly<{
    post: schemas["Post"]
    children?: ReactNode
}>

export function PostPreview({ post, children }: PostPreviewProps) {
    return (
        <div className="flex gap-3">
            <div className="flex flex-col items-center">
                <PostAvatar
                    author={post.author}
                    interactive={false}
                />
                <div className="bg-border mt-1 w-0.5 flex-1" />
            </div>
            <div className="min-w-0 flex-1 pb-8">
                <PostMetadata
                    author={post.author}
                    createdAt={post.created_at}
                    interactive={false}
                />
                <p className="mt-0.5 text-[15px] wrap-break-word whitespace-pre-wrap">
                    <PostText
                        text={post.text}
                        entities={post.entities}
                        interactive={false}
                    />
                </p>
                {children}
            </div>
        </div>
    )
}
