import { Link } from "@tanstack/react-router"

import {
    SidebarGroup,
    SidebarGroupContent,
    SidebarMenu,
    SidebarMenuButton,
    SidebarMenuItem
} from "@/libs/ui/components/sidebar"

import { options } from "./sidebar-nav.config"

export function Navigation() {
    return (
        <SidebarGroup>
            <SidebarGroupContent>
                <SidebarMenu>
                    {options.map((option) => (
                        <SidebarMenuItem key={option.to}>
                            <SidebarMenuButton
                                asChild
                                tooltip={option.label}
                            >
                                <Link
                                    to={option.to}
                                    inactiveProps={{ className: "opacity-60" }}
                                >
                                    <option.icon />
                                    <span>{option.label}</span>
                                </Link>
                            </SidebarMenuButton>
                        </SidebarMenuItem>
                    ))}
                </SidebarMenu>
            </SidebarGroupContent>
        </SidebarGroup>
    )
}
