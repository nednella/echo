import type { Theme } from "./types"

/**
 * Media query handle for the user's OS color scheme preference.
 */
const PREFERS_DARK_MODE_MQ = globalThis.matchMedia("(prefers-color-scheme: dark)")

/**
 * Resolves the current theme from state.
 *
 * If theme is equal to `system`, this function will check the user's OS
 * preference and return that preference.
 *
 * For any other theme value, the input itself is returned unchanged.
 *
 * @param theme the current theme from state
 * @returns "light" or "dark"
 */
function resolveTheme(theme: Theme): Theme {
    if (theme !== "system") return theme
    return PREFERS_DARK_MODE_MQ.matches ? "dark" : "light"
}

/**
 * Apply a theme to the document root element via a `data-theme` attribute.
 *
 * @param theme the theme to apply
 */
function applyTheme(theme: Theme) {
    document.documentElement.dataset.theme = resolveTheme(theme)
}

/**
 * Subscribe to changes in the user's OS colour scheme preference.
 *
 * The provided callback function is invoked whenever
 * `(prefers-color-scheme: dark)` is toggled.
 *
 * @param callback the function to run when the media query is updated
 * @returns a cleanup function that removes the event listener
 */
function onSystemThemeChange(callback: () => void) {
    const mq = PREFERS_DARK_MODE_MQ

    const handleChange = () => callback()

    mq.addEventListener("change", handleChange)
    return () => mq.removeEventListener("change", handleChange)
}

export { resolveTheme, applyTheme, onSystemThemeChange }
