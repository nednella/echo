import { useUser } from "@clerk/clerk-react"
import { useMutation, useQueryClient } from "@tanstack/react-query"
import { CalendarDays, MapPin } from "lucide-react"
import { toast } from "sonner"

import { UserAvatar } from "@/components/user-avatar"
import { toggleFollowMutationOptions } from "@/features/profile/api/options"
import { ProfileActions } from "@/features/profile/components/profile-actions"
import { ApiException } from "@/libs/api/exception"
import type { schemas } from "@/libs/api/openapi-client"
import { Button } from "@/libs/ui/components/button"
import { useFollowListDialog } from "@/stores/follow-list-dialog.store"
import { useUpdateProfileDialog } from "@/stores/update-profile-dialog.store"

type ProfileHeaderProps = Readonly<{
    profile: schemas["Profile"]
}>

export function ProfileHeader({ profile }: ProfileHeaderProps) {
    const { user } = useUser()
    const isOwn = profile.id === user?.externalId
    const { onOpen } = useUpdateProfileDialog()
    const { onOpen: openFollowList } = useFollowListDialog()
    const queryClient = useQueryClient()

    const { mutate: toggleFollow, isPending } = useMutation({
        ...toggleFollowMutationOptions(),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["profile", profile.username] })
        },
        onError: (error) => {
            const description = error instanceof ApiException ? error.message : "Please try again later"
            toast.error("Could not update follow status", { description })
        }
    })

    const joinedDate = new Date(profile.created_at).toLocaleDateString(undefined, {
        month: "long",
        year: "numeric"
    })

    return (
        <div className="border-b p-4">
            <div className="flex items-start justify-between">
                <UserAvatar
                    src={profile.image_url}
                    alt={profile.username}
                    className="size-20 shrink-0"
                />

                <div className="flex items-center gap-2">
                    <ProfileActions username={profile.username} />
                    {isOwn ? (
                        <Button
                            variant="outline"
                            rounded
                            onClick={onOpen}
                        >
                            Edit profile
                        </Button>
                    ) : (
                        <Button
                            rounded
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
            </div>

            <div className="mt-3 flex flex-col gap-2">
                <div>
                    <p className="text-lg font-bold">{profile.name}</p>
                    <p className="text-muted-foreground text-sm">@{profile.username}</p>
                </div>

                {profile.bio && <p className="text-sm whitespace-pre-wrap">{profile.bio}</p>}

                <div className="text-muted-foreground flex flex-wrap gap-x-4 gap-y-1 text-sm">
                    {profile.location && (
                        <span className="flex items-center gap-1">
                            <MapPin className="size-3.5" />
                            {profile.location}
                        </span>
                    )}
                    <span className="flex items-center gap-1">
                        <CalendarDays className="size-3.5" />
                        Joined {joinedDate}
                    </span>
                </div>

                <div className="flex gap-4 text-sm">
                    <button
                        type="button"
                        className="cursor-pointer hover:underline"
                        onClick={() => openFollowList(profile.id, "following")}
                    >
                        <strong>{profile.metrics.following}</strong>{" "}
                        <span className="text-muted-foreground">Following</span>
                    </button>
                    <button
                        type="button"
                        className="cursor-pointer hover:underline"
                        onClick={() => openFollowList(profile.id, "followers")}
                    >
                        <strong>{profile.metrics.followers}</strong>{" "}
                        <span className="text-muted-foreground">Followers</span>
                    </button>
                </div>
            </div>
        </div>
    )
}
