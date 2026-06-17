import { Heart, MessageCircle } from "lucide-react"

import { cn } from "@/libs/ui/utils"

type PostInteractionsProps = Readonly<{
    replies: number
    likes: number
    liked: boolean
    className?: string
}>

export function PostInteractions({ replies, likes, liked, className }: PostInteractionsProps) {
    return (
        <div className={cn("text-muted-foreground flex items-center gap-8 text-sm", className)}>
            <span className="flex items-center gap-1.5">
                <MessageCircle className="size-4" />
                {replies}
            </span>
            <span className={cn("flex items-center gap-1.5", liked && "text-destructive")}>
                <Heart className={cn("size-4", liked && "fill-current")} />
                {likes}
            </span>
        </div>
    )
}
