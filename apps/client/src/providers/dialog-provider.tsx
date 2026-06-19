import { CreatePostDialog } from "@/features/post/components/create-post-dialog"
import { FollowListDialog } from "@/features/profile/components/follow-list-dialog"
import { UpdateProfileDialog } from "@/features/profile/components/update-profile-dialog"
import { AppearanceDialog } from "@/features/settings/appearance/components/appearance-dialog"

export function DialogProvider() {
    return (
        <>
            <AppearanceDialog />
            <CreatePostDialog />
            <FollowListDialog />
            <UpdateProfileDialog />
        </>
    )
}
