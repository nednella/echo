import { useMutation, useQueryClient } from "@tanstack/react-query"
import { Heart, MessageCircle } from "lucide-react"
import { toast } from "sonner"

import { toggleLikeMutationOptions } from "@/features/post/api/options"
import { ApiException } from "@/libs/api/exception"
import { cn } from "@/libs/ui/utils"

type PostInteractionsProps = Readonly<{
    postId: string
    replies: number
    likes: number
    liked: boolean
    onReply: () => void
    className?: string
}>

export function PostInteractions({ postId, replies, likes, liked, onReply, className }: PostInteractionsProps) {
    const queryClient = useQueryClient()

    const { mutate } = useMutation({
        ...toggleLikeMutationOptions(),
        onSuccess: () => queryClient.invalidateQueries({ queryKey: ["feed"] }),
        onError: (error) =>
            toast.error("Could not update like", {
                description: error instanceof ApiException ? error.message : "Please try again later"
            })
    })

    return (
        <div className={cn("text-muted-foreground flex items-center gap-8 text-sm", className)}>
            <button
                type="button"
                onClick={onReply}
                className="group hover:text-echo-500 flex cursor-pointer items-center gap-1.5 transition-colors"
            >
                <MessageCircle className="size-4 transition group-hover:drop-shadow-[0_0_7px_var(--color-echo-500)]" />
                {replies}
            </button>
            <button
                type="button"
                onClick={() => mutate({ id: postId, liked })}
                className={cn(
                    "group hover:text-destructive flex cursor-pointer items-center gap-1.5 transition-colors",
                    liked && "text-destructive"
                )}
            >
                <Heart
                    className={cn(
                        "size-4 transition group-hover:drop-shadow-[0_0_7px_var(--color-destructive)]",
                        liked && "fill-current"
                    )}
                />
                {likes}
            </button>
        </div>
    )
}
