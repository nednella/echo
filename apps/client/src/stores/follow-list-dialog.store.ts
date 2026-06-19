import { create } from "zustand"

export type FollowList = "followers" | "following"

type FollowListDialogState = {
    isOpen: boolean
    profileId: string | undefined
    tab: FollowList
}

type FollowListDialogActions = {
    onOpen: (profileId: string, tab: FollowList) => void
    setTab: (tab: FollowList) => void
    onClose: () => void
}

export const useFollowListDialog = create<FollowListDialogState & FollowListDialogActions>((set) => ({
    isOpen: false,
    profileId: undefined,
    tab: "followers",
    onOpen: (profileId, tab) => set({ isOpen: true, profileId, tab }),
    setTab: (tab) => set({ tab }),
    onClose: () => set({ isOpen: false })
}))
