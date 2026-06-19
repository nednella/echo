import { useMutation, useQueryClient } from "@tanstack/react-query"
import { toast } from "sonner"

import { deletePostMutationOptions } from "@/features/post/api/options"
import { ApiException } from "@/libs/api/exception"
import {
    AlertDialog,
    AlertDialogCancel,
    AlertDialogContent,
    AlertDialogDescription,
    AlertDialogFooter,
    AlertDialogHeader,
    AlertDialogTitle
} from "@/libs/ui/components/alert-dialog"
import { Button } from "@/libs/ui/components/button"
import { useDeletePostDialog } from "@/stores/delete-post-dialog.store"

export function DeletePostDialog() {
    const { isOpen, postId, onClose } = useDeletePostDialog()
    const queryClient = useQueryClient()

    const { mutate, isPending } = useMutation({
        ...deletePostMutationOptions(),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["feed"] })
            toast.success("Post deleted")
            onClose()
        },
        onError: (error) =>
            toast.error("Could not delete post", {
                description: error instanceof ApiException ? error.message : "Please try again later"
            })
    })

    const onOpenChange = (open: boolean) => {
        if (!open) onClose()
    }

    return (
        <AlertDialog
            open={isOpen}
            onOpenChange={onOpenChange}
        >
            <AlertDialogContent>
                <AlertDialogHeader>
                    <AlertDialogTitle>Delete post?</AlertDialogTitle>
                    <AlertDialogDescription>This action cannot be undone.</AlertDialogDescription>
                </AlertDialogHeader>
                <AlertDialogFooter>
                    <AlertDialogCancel>Cancel</AlertDialogCancel>
                    <Button
                        variant="destructive"
                        disabled={isPending}
                        onClick={() => postId && mutate({ id: postId })}
                    >
                        Delete
                    </Button>
                </AlertDialogFooter>
            </AlertDialogContent>
        </AlertDialog>
    )
}
