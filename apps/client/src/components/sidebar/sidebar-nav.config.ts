import { linkOptions } from "@tanstack/react-router"
import { House, MessageSquare, Search, UserRound } from "lucide-react"

const options = linkOptions([
    {
        icon: House,
        label: "Home",
        to: "/home"
    },
    {
        icon: Search,
        label: "Search",
        to: "/onboarding" // TODO: temporary avoid type error
    },
    {
        icon: UserRound,
        label: "Profile",
        to: "/onboarding" // TODO: temporary avoid type error
    },
    {
        icon: MessageSquare,
        label: "Chat",
        to: "/onboarding" // TODO: temporary avoid type error
    }
])

export { options }
