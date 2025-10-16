import { useUser } from "@clerk/clerk-react"
import { ChevronsUpDown, UserRound } from "lucide-react"

import { Avatar, AvatarFallback, AvatarImage } from "@/libs/ui/components/avatar"
import { SidebarMenuButton } from "@/libs/ui/components/sidebar"

import { AccountMenu } from "../dropdowns/account-menu"

export function Footer() {
    const { user } = useUser()

    return (
        <AccountMenu
            trigger={
                <SidebarMenuButton
                    size="lg"
                    className="data-[state=open]:bg-sidebar-accent data-[state=open]:text-sidebar-accent-foreground
                        cursor-pointer"
                >
                    <Avatar>
                        <AvatarImage
                            src={user?.imageUrl}
                            alt={user?.username || undefined}
                        />
                        <AvatarFallback>
                            <UserRound size={16} />
                        </AvatarFallback>
                    </Avatar>
                    <div className="grid flex-1 text-left text-sm leading-tight">
                        <span className="truncate font-medium">{user?.username}</span>
                        <span className="truncate text-xs">{user?.primaryEmailAddress?.emailAddress}</span>
                    </div>
                    <ChevronsUpDown className="ml-auto size-4" />
                </SidebarMenuButton>
            }
        />
    )
}
