import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle } from "@/libs/ui/components/dialog"
import { useCreatePostDialog } from "@/stores/create-post-dialog.store"

import { PostComposer } from "./post-composer"

export function CreatePostDialog() {
    const { isOpen, onClose } = useCreatePostDialog()

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
                    <DialogTitle>Create post</DialogTitle>
                    <DialogDescription>{"Share what's happening"}</DialogDescription>
                </DialogHeader>
                <PostComposer
                    autoFocus
                    onPosted={onClose}
                />
            </DialogContent>
        </Dialog>
    )
}
