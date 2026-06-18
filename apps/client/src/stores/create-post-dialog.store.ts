import { create } from "zustand"

import type { schemas } from "@/libs/api/openapi-client"

type DialogState = {
    isOpen: boolean
    inReplyTo?: schemas["Post"]
}

type DialogActions = {
    onOpen: (inReplyTo?: schemas["Post"]) => void
    onClose: () => void
}

export const useCreatePostDialog = create<DialogState & DialogActions>((set) => ({
    isOpen: false,
    inReplyTo: undefined,
    onOpen: (inReplyTo) =>
        set({
            isOpen: true,
            inReplyTo
        }),
    onClose: () =>
        set({
            isOpen: false,
            inReplyTo: undefined
        })
}))
