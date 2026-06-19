import { useUser } from "@clerk/clerk-react"
import { useMutation, useQueryClient } from "@tanstack/react-query"
import { Link } from "@tanstack/react-router"
import { toast } from "sonner"

import { UserAvatar } from "@/components/user-avatar"
import { toggleFollowMutationOptions } from "@/features/profile/api/options"
import { ApiException } from "@/libs/api/exception"
import type { schemas } from "@/libs/api/openapi-client"
import { Button } from "@/libs/ui/components/button"
import { useFollowListDialog } from "@/stores/follow-list-dialog.store"

type ProfileListItemProps = Readonly<{
    profile: schemas["SimplifiedProfile"]
}>

export function ProfileListItem({ profile }: ProfileListItemProps) {
    const { user } = useUser()
    const { onClose } = useFollowListDialog()
    const queryClient = useQueryClient()

    const { mutate: toggleFollow, isPending } = useMutation({
        ...toggleFollowMutationOptions(),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["profile"] })
        },
        onError: (error) => {
            const description = error instanceof ApiException ? error.message : "Please try again later"
            toast.error("Could not update follow status", { description })
        }
    })

    const isOwn = profile.username === user?.username

    return (
        <div className="flex items-start gap-3 px-4 py-3">
            <Link
                to="/profile/$username"
                params={{ username: profile.username }}
                className="flex min-w-0 flex-1 items-start gap-3"
                onClick={onClose}
            >
                <UserAvatar
                    src={profile.image_url}
                    alt={profile.username}
                    className="size-10 shrink-0"
                />
                <div className="min-w-0">
                    {profile.name && <p className="truncate text-sm font-medium">{profile.name}</p>}
                    <p className="text-muted-foreground truncate text-sm">@{profile.username}</p>
                    {profile.bio && <p className="mt-0.5 line-clamp-2 text-sm">{profile.bio}</p>}
                </div>
            </Link>

            {isOwn ? (
                <span className="bg-muted text-muted-foreground self-center rounded-full px-3 py-1 text-xs font-medium">
                    You
                </span>
            ) : (
                <Button
                    size="sm"
                    rounded
                    className="self-center"
                    disabled={isPending}
                    onClick={() =>
                        toggleFollow({
                            id: profile.id,
                            following: profile.relationship?.following ?? false
                        })
                    }
                >
                    {profile.relationship?.following ? "Following" : "Follow"}
                </Button>
            )}
        </div>
    )
}
