import type { Theme } from "@/libs/theme/types"

type ThemeOption = {
    theme: Theme
    label: string
    src: string
}

const options: ThemeOption[] = [
    {
        theme: "light",
        label: "Light",
        src: "/theme-thumbnails/light_mode.png"
    },
    {
        theme: "system",
        label: "System",
        src: "/theme-thumbnails/system_mode.png"
    },
    {
        theme: "dark",
        label: "Dark",
        src: "/theme-thumbnails/dark_mode.png"
    }
]

export { type ThemeOption, options }
