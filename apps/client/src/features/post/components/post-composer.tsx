import { useState } from "react"

import { useUser } from "@clerk/clerk-react"
import { useMutation, useQueryClient } from "@tanstack/react-query"
import { toast } from "sonner"

import { AccountAvatar } from "@/features/account/components/account-avatar"
import { createPostMutationOptions } from "@/features/post/api/options"
import { MAX_POST_LENGTH } from "@/features/post/constants"
import { ApiException } from "@/libs/api/exception"
import { Button } from "@/libs/ui/components/button"
import { Textarea } from "@/libs/ui/components/textarea"
import { cn } from "@/libs/ui/utils"

interface Props {
    autoFocus?: boolean
    onPosted?: () => void
}

export function PostComposer({ autoFocus = false, onPosted }: Readonly<Props>) {
    const { user } = useUser()
    const [text, setText] = useState("")
    const queryClient = useQueryClient()

    const { mutate, isPending } = useMutation({
        ...createPostMutationOptions(),
        onSuccess: () => {
            setText("")
            queryClient.invalidateQueries({ queryKey: ["feed"] })
            toast.success("Your post was published")
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

    const handleSubmit = () => {
        if (!canSubmit) return
        mutate({ text: trimmed })
    }

    const handleKeyDown = (event: React.KeyboardEvent<HTMLTextAreaElement>) => {
        if ((event.metaKey || event.ctrlKey) && event.key === "Enter") {
            event.preventDefault()
            handleSubmit()
        }
    }

    return (
        <div className="flex w-full gap-3">
            <AccountAvatar className="size-10" />
            <div className="flex flex-1 flex-col gap-2">
                <Textarea
                    autoFocus={autoFocus}
                    rows={1}
                    value={text}
                    onChange={(event) => setText(event.target.value)}
                    onKeyDown={handleKeyDown}
                    placeholder={`What's on your mind, ${user?.username}?`}
                    aria-invalid={isOverLimit}
                    disabled={isPending}
                    className="min-h-0 resize-none border-0 bg-transparent px-0 py-2 text-base shadow-none
                        focus-visible:ring-0 md:text-base dark:bg-transparent"
                />
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
                        {isPending ? "Posting…" : "Post"}
                    </Button>
                </div>
            </div>
        </div>
    )
}
