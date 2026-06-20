import { useState } from "react"

import { useUser } from "@clerk/clerk-react"
import { useMutation, useQueryClient } from "@tanstack/react-query"
import { toast } from "sonner"

import { UserAvatar } from "@/components/user-avatar"
import { createPostMutationOptions } from "@/features/post/api/options"
import { PostText } from "@/features/post/components/post-text"
import { MAX_POST_LENGTH } from "@/features/post/constants"
import { ApiException } from "@/libs/api/exception"
import { Button } from "@/libs/ui/components/button"
import { Textarea } from "@/libs/ui/components/textarea"
import { cn } from "@/libs/ui/utils"

type PostComposerProps = Readonly<{
    autoFocus?: boolean
    onPosted?: () => void
    parentId?: string
    placeholder?: string
}>

export function PostComposer({ autoFocus = false, onPosted, parentId, placeholder }: PostComposerProps) {
    const { user } = useUser()
    const [text, setText] = useState("")
    const queryClient = useQueryClient()

    const { mutate, isPending } = useMutation({
        ...createPostMutationOptions(),
        onSuccess: () => {
            setText("")
            queryClient.invalidateQueries({ queryKey: ["feed"] })
            onPosted?.()
        },
        onError: (error) => {
            const description = error instanceof ApiException ? error.message : "Please try again later"
            toast.error("Could not publish your post", { description })
        }
    })

    const trimmed = text.trim()
    const remaining = MAX_POST_LENGTH - text.length
    const isOverLimit = remaining < 0
    const canSubmit = trimmed.length > 0 && !isOverLimit && !isPending

    const idleLabel = parentId ? "Reply" : "Post"
    const pendingLabel = parentId ? "Replying…" : "Posting…"
    const submitLabel = isPending ? pendingLabel : idleLabel

    const handleSubmit = () => {
        if (!canSubmit) return
        mutate({
            text: trimmed,
            parent_id: parentId
        })
    }

    const handleKeyDown = (event: React.KeyboardEvent<HTMLTextAreaElement>) => {
        if ((event.metaKey || event.ctrlKey) && event.key === "Enter") {
            event.preventDefault()
            handleSubmit()
        }
    }

    return (
        <div className="flex w-full gap-3">
            <UserAvatar
                src={user?.imageUrl}
                alt={user?.username ?? undefined}
                className="size-10"
            />
            <div className="flex min-w-0 flex-1 flex-col gap-2">
                <div className="relative">
                    <div
                        aria-hidden
                        className="pointer-events-none absolute inset-0 py-2 text-base wrap-anywhere
                            whitespace-pre-wrap"
                    >
                        <PostText
                            text={text}
                            interactive={false}
                        />
                    </div>
                    <Textarea
                        autoFocus={autoFocus}
                        rows={1}
                        value={text}
                        onChange={(event) => setText(event.target.value)}
                        onKeyDown={handleKeyDown}
                        placeholder={placeholder ?? `What's on your mind, ${user?.username}?`}
                        aria-invalid={isOverLimit}
                        disabled={isPending}
                        className="caret-foreground relative min-h-0 resize-none border-0 bg-transparent px-0 py-2
                            text-base text-transparent shadow-none focus-visible:ring-0 md:text-base"
                    />
                </div>
                <div className="flex items-center justify-end gap-3">
                    <span
                        className={cn(
                            "text-muted-foreground text-sm tabular-nums",
                            remaining <= 20 && "text-foreground",
                            isOverLimit && "text-destructive"
                        )}
                    >
                        {remaining}
                    </span>
                    <Button
                        rounded
                        disabled={!canSubmit}
                        onClick={handleSubmit}
                    >
                        {submitLabel}
                    </Button>
                </div>
            </div>
        </div>
    )
}
