import { create } from "zustand"
import { persist } from "zustand/middleware"

import type { Theme } from "./types"
import { resolveTheme } from "./utils"

type ThemeState = {
    theme: Theme
}

type ThemeActions = {
    setTheme: (t: Theme) => void
    toggleTheme: () => void
}

export const useThemeStore = create<ThemeState & ThemeActions>()(
    persist(
        (set, get) => ({
            theme: "system",
            setTheme: (t) => set({ theme: t }),
            toggleTheme: () => {
                const current = resolveTheme(get().theme)
                set({ theme: current === "light" ? "dark" : "light" })
            }
        }),
        { name: "ui-theme" }
    )
)
