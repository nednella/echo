import { PostAvatar } from "@/features/post/components/post-avatar"
import { PostInteractions } from "@/features/post/components/post-interactions"
import { PostMetadata } from "@/features/post/components/post-metadata"
import { PostText } from "@/features/post/components/post-text"
import type { schemas } from "@/libs/api/openapi-client"
import { useCreatePostDialog } from "@/stores/create-post-dialog.store"

type PostProps = Readonly<{ post: schemas["Post"] }>

export function Post({ post }: PostProps) {
    const onOpen = useCreatePostDialog((state) => state.onOpen)

    return (
        <article className="relative flex gap-3 px-4 py-3">
            <PostAvatar author={post.author} />
            <div className="min-w-0 flex-1">
                <PostMetadata
                    author={post.author}
                    createdAt={post.created_at}
                />
                <p className="mt-0.5 text-[15px] wrap-break-word whitespace-pre-wrap">
                    <PostText
                        text={post.text}
                        entities={post.entities}
                    />
                </p>
                <PostInteractions
                    postId={post.id}
                    replies={post.metrics.replies}
                    likes={post.metrics.likes}
                    liked={post.relationship.liked}
                    onReply={() => onOpen(post)}
                    className="mt-2"
                />
            </div>
            <div className="via-border absolute inset-x-0 bottom-0 h-px bg-linear-to-r from-transparent to-transparent" />
        </article>
    )
}
