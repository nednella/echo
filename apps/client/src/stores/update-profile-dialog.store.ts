import { create } from "zustand"

type DialogState = {
    isOpen: boolean
}

type DialogActions = {
    onOpen: () => void
    onClose: () => void
}

export const useUpdateProfileDialog = create<DialogState & DialogActions>((set) => ({
    isOpen: false,
    onOpen: () => set({ isOpen: true }),
    onClose: () => set({ isOpen: false })
}))
