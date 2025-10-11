import { create } from "zustand"
import { persist } from "zustand/middleware"

import type { Theme } from "./types"

type ThemeState = {
    theme: Theme
}

type ThemeActions = {
    setTheme: (t: Theme) => void
}

export const useThemeStore = create<ThemeState & ThemeActions>()(
    persist(
        (set) => ({
            theme: "system",
            setTheme: (t) => set({ theme: t })
        }),
        { name: "ui-theme" }
    )
)
