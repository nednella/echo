import React from "react"

import { useClerk } from "@clerk/clerk-react"
import { IdCard, LogOut, Paintbrush } from "lucide-react"

import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuSeparator,
    DropdownMenuTrigger
} from "@/libs/ui/components/dropdown-menu"
import { useAppearanceDialog } from "@/stores/appearance-dialog.store"

type AccountMenuProps = Readonly<{
    children: React.ReactNode
    align?: "start" | "center" | "end"
}>

export function AccountMenu({ children, align = "end" }: AccountMenuProps) {
    const { signOut, openUserProfile } = useClerk()
    const { onOpen: openAppearanceDialog } = useAppearanceDialog()

    const handleLogout = () => signOut()

    return (
        <DropdownMenu>
            <DropdownMenuTrigger asChild>{children}</DropdownMenuTrigger>
            <DropdownMenuContent
                className="w-56 rounded-lg"
                side="bottom"
                align={align}
                sideOffset={10}
            >
                <DropdownMenuItem onSelect={() => openUserProfile()}>
                    <IdCard />
                    <span>Account</span>
                </DropdownMenuItem>
                <DropdownMenuItem onSelect={() => openAppearanceDialog()}>
                    <Paintbrush />
                    <span>Appearance</span>
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
