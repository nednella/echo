import React from "react"

import { useClerk } from "@clerk/clerk-react"
import { IdCard, LogOut, Settings } from "lucide-react"

import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuSeparator,
    DropdownMenuTrigger
} from "@/libs/ui/components/dropdown-menu"

interface Props {
    trigger: React.ReactNode
}

export function AccountMenu({ trigger }: Readonly<Props>) {
    const { signOut, openUserProfile } = useClerk()

    const handleLogout = () => signOut()

    return (
        <DropdownMenu>
            <DropdownMenuTrigger asChild>{trigger}</DropdownMenuTrigger>
            <DropdownMenuContent
                className="w-(--radix-dropdown-menu-trigger-width) min-w-56 rounded-lg"
                side="top"
                align="start"
                sideOffset={8}
            >
                <DropdownMenuItem onSelect={() => openUserProfile()}>
                    <IdCard />
                    <span>Account</span>
                </DropdownMenuItem>
                <DropdownMenuItem onSelect={() => console.log("Settings")}>
                    <Settings />
                    <span>Settings</span>
                </DropdownMenuItem>
                <DropdownMenuSeparator className="mx-px" />
                <DropdownMenuItem
                    onClick={handleLogout}
                    variant="destructive"
                    className="cursor-pointer"
                >
                    <LogOut />
                    <span>Log out</span>
                </DropdownMenuItem>
            </DropdownMenuContent>
        </DropdownMenu>
    )
}
