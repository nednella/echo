import { CreatePostDialog } from "@/features/post/components/create-post-dialog"
import { AppearanceDialog } from "@/features/settings/appearance/components/appearance-dialog"

export function DialogProvider() {
    return (
        <>
            <AppearanceDialog />
            <CreatePostDialog />
        </>
    )
}
