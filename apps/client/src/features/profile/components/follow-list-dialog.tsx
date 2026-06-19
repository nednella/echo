import { useRef } from "react"

import { followersInfiniteQueryOptions, followingInfiniteQueryOptions } from "@/features/profile/api/options"
import { ProfileList } from "@/features/profile/components/profile-list"
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle } from "@/libs/ui/components/dialog"
import { cn } from "@/libs/ui/utils"
import { type FollowList, useFollowListDialog } from "@/stores/follow-list-dialog.store"

const tabs: { id: FollowList; label: string }[] = [
    { id: "followers", label: "Followers" },
    { id: "following", label: "Following" }
]

export function FollowListDialog() {
    const { isOpen, profileId, tab, setTab, onClose } = useFollowListDialog()
    const scrollReference = useRef<HTMLDivElement>(null)

    return (
        <Dialog
            open={isOpen}
            onOpenChange={(open) => {
                if (!open) onClose()
            }}
        >
            <DialogContent className="flex max-h-[80vh] flex-col gap-0 p-0 sm:max-w-md">
                <DialogHeader className="sr-only">
                    <DialogTitle>{tab === "followers" ? "Followers" : "Following"}</DialogTitle>
                    <DialogDescription>
                        {tab === "followers" ? "People who follow this profile" : "People this profile follows"}
                    </DialogDescription>
                </DialogHeader>

                <div className="flex border-b">
                    {tabs.map((t) => (
                        <button
                            key={t.id}
                            type="button"
                            onClick={() => setTab(t.id)}
                            className={cn(
                                `relative flex flex-1 items-center justify-center py-4 text-sm font-medium
                                transition-colors`,
                                tab === t.id ? "text-foreground" : "text-muted-foreground"
                            )}
                        >
                            {t.label}
                            <span
                                className={cn(
                                    `from-echo-400 to-echo-600 absolute bottom-0 h-0.5 w-full rounded-full
                                    bg-linear-to-r transition-opacity`,
                                    tab === t.id ? "opacity-100" : "opacity-0"
                                )}
                            />
                        </button>
                    ))}
                </div>

                <div
                    ref={scrollReference}
                    className="overflow-y-auto"
                >
                    {profileId !== undefined && (
                        <ProfileList
                            key={tab}
                            scrollRootRef={scrollReference as React.RefObject<HTMLElement | null>}
                            options={
                                tab === "followers"
                                    ? followersInfiniteQueryOptions(profileId)
                                    : followingInfiniteQueryOptions(profileId)
                            }
                            emptyMessage={tab === "followers" ? "No followers yet." : "Not following anyone yet."}
                        />
                    )}
                </div>
            </DialogContent>
        </Dialog>
    )
}
