import { useEffect } from "react"

import { useThemeStore } from "@/common/stores/theme.store"
import { applyTheme, onSystemThemeChange } from "@/common/utils/theme"

export function ThemeProvider() {
    const { theme } = useThemeStore()

    /**
     * Applies the new theme to the DOM as soon as it's updated,
     * and if set to system, appends an event listener for OS theme
     * preference changes.
     *
     * The listener is removed on any re-render when the deps change.
     */
    useEffect(() => {
        applyTheme(theme)

        if (theme !== "system") return
        const unsubscribe = onSystemThemeChange(() => applyTheme("system"))
        return unsubscribe
    }, [theme])

    // eslint-disable-next-line unicorn/no-null
    return null
}
