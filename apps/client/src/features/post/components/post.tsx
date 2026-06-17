import { Heart, MessageCircle, UserRound } from "lucide-react"

import { PostText } from "@/features/post/components/post-text"
import type { schemas } from "@/libs/api/openapi-client"
import { Avatar, AvatarFallback, AvatarImage } from "@/libs/ui/components/avatar"
import { cn } from "@/libs/ui/utils"
import { relativeTime } from "@/utils/datetime"

type PostProps = Readonly<{ post: schemas["Post"] }>

export function Post({ post }: PostProps) {
    const { author, metrics, relationship } = post

    return (
        <article className="relative flex cursor-pointer gap-3 px-4 py-3">
            <Avatar className="size-10 shrink-0">
                <AvatarImage
                    src={author.image_url}
                    alt={author.username}
                />
                <AvatarFallback>
                    <UserRound className="size-1/2" />
                </AvatarFallback>
            </Avatar>
            <div className="min-w-0 flex-1">
                <div className="flex items-baseline gap-1.5 text-sm">
                    <span className="truncate font-semibold">{author.name ?? author.username}</span>
                    <span className="text-muted-foreground truncate">@{author.username}</span>
                    <span className="text-muted-foreground">·</span>
                    <span className="text-muted-foreground shrink-0">{relativeTime(post.created_at)}</span>
                </div>
                <p className="mt-0.5 text-[15px] wrap-break-word whitespace-pre-wrap">
                    <PostText
                        text={post.text}
                        entities={post.entities}
                    />
                </p>
                <div className="text-muted-foreground mt-2 flex items-center gap-8 text-sm">
                    <span className="flex items-center gap-1.5">
                        <MessageCircle className="size-4" />
                        {metrics.replies}
                    </span>
                    <span className={cn("flex items-center gap-1.5", relationship.liked && "text-destructive")}>
                        <Heart className={cn("size-4", relationship.liked && "fill-current")} />
                        {metrics.likes}
                    </span>
                </div>
            </div>
            <div className="via-border absolute inset-x-0 bottom-0 h-px bg-linear-to-r from-transparent to-transparent" />
        </article>
    )
}
