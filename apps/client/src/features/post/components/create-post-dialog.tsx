import { PostComposer } from "@/features/post/components/post-composer"
import { PostPreview } from "@/features/post/components/post-preview"
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle } from "@/libs/ui/components/dialog"
import { useCreatePostDialog } from "@/stores/create-post-dialog.store"

export function CreatePostDialog() {
    const { isOpen, inReplyTo, onClose } = useCreatePostDialog()

    const onOpenChange = (open: boolean) => {
        if (!open) onClose()
    }

    return (
        <Dialog
            open={isOpen}
            onOpenChange={onOpenChange}
        >
            <DialogContent className="sm:max-w-xl">
                <DialogHeader className="sr-only">
                    <DialogTitle>{inReplyTo ? "Reply" : "Create post"}</DialogTitle>
                    <DialogDescription>{inReplyTo ? "Reply to this post" : "Share what's happening"}</DialogDescription>
                </DialogHeader>
                <div className="flex min-w-0 flex-col">
                    {inReplyTo && (
                        <PostPreview post={inReplyTo}>
                            <p className="text-muted-foreground mt-3 text-sm">
                                Replying to <span className="text-echo-500">@{inReplyTo.author.username}</span>
                            </p>
                        </PostPreview>
                    )}
                    <PostComposer
                        parentId={inReplyTo?.id}
                        placeholder={inReplyTo ? "Post your reply" : undefined}
                        autoFocus
                        onPosted={onClose}
                    />
                </div>
            </DialogContent>
        </Dialog>
    )
}
