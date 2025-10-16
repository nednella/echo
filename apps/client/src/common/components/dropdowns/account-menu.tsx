import React from "react"

import { useAuth } from "@clerk/clerk-react"
import { Link } from "@tanstack/react-router"
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
    const { signOut } = useAuth()

    const handleLogout = () => signOut()

    return (
        <DropdownMenu>
            <DropdownMenuTrigger asChild>{trigger}</DropdownMenuTrigger>
            <DropdownMenuContent
                className="w-(--radix-dropdown-menu-trigger-width) rounded-lg"
                side="top"
                align="start"
                sideOffset={8}
            >
                <DropdownMenuItem asChild>
                    <Link
                        to="/onboarding" // TODO: temporary avoid type error
                        className="cursor-pointer"
                    >
                        <IdCard />
                        <span>Account</span>
                    </Link>
                </DropdownMenuItem>
                <DropdownMenuItem asChild>
                    <Link
                        to="/onboarding" // TODO: temporary avoid type error
                        className="cursor-pointer"
                    >
                        <Settings />
                        <span>Settings</span>
                    </Link>
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
