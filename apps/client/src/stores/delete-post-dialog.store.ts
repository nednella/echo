import { create } from "zustand"

type DialogState = {
    isOpen: boolean
    postId?: string
}

type DialogActions = {
    onOpen: (postId: string) => void
    onClose: () => void
}

export const useDeletePostDialog = create<DialogState & DialogActions>((set) => ({
    isOpen: false,
    postId: undefined,
    onOpen: (postId) =>
        set({
            isOpen: true,
            postId
        }),
    onClose: () =>
        set({
            isOpen: false,
            postId: undefined
        })
}))
