import { useUser } from "@clerk/clerk-react"
import { PenLine } from "lucide-react"

import { UserAvatar } from "@/components/user-avatar"
import { Button } from "@/libs/ui/components/button"
import { useCreatePostDialog } from "@/stores/create-post-dialog.store"

export function PostComposerPrompt() {
    const { user } = useUser()
    const { onOpen } = useCreatePostDialog()

    return (
        <div className="hidden border-b px-4 py-4 lg:block">
            <div className="flex w-full items-center gap-3">
                <UserAvatar
                    src={user?.imageUrl}
                    alt={user?.username ?? undefined}
                    className="size-10"
                />
                <button
                    type="button"
                    onClick={() => onOpen()}
                    className="bg-muted/60 text-muted-foreground hover:bg-muted h-11 flex-1 cursor-pointer rounded-full
                        px-5 text-left text-base transition-colors"
                >
                    {`What's on your mind, ${user?.username}?`}
                </button>
            </div>
        </div>
    )
}

export function PostComposerButton() {
    const { onOpen } = useCreatePostDialog()

    return (
        <Button
            variant="custom"
            onClick={() => onOpen()}
            aria-label="Create post"
            className="from-echo-400 to-echo-600 fixed right-5 bottom-5 z-30 size-14 rounded-full bg-linear-to-br
                text-white shadow-[0_8px_24px_-6px_var(--color-echo-500)] hover:brightness-110 lg:hidden"
        >
            <PenLine className="size-6" />
        </Button>
    )
}
