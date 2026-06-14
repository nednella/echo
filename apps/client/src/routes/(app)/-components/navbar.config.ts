import { Bell, House, type LucideIcon, Search } from "lucide-react"

import type { FileRouteTypes } from "@/routeTree.gen"

export interface NavItem {
    label: string
    icon: LucideIcon
    to: FileRouteTypes["to"]
}

export const navItems: NavItem[] = [
    { label: "Home", icon: House, to: "/home" },
    { label: "Search", icon: Search, to: "/login" },
    { label: "Notifications", icon: Bell, to: "/login" }
]
