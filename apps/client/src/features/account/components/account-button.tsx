import { useUser } from "@clerk/clerk-react"
import { AvatarFallback, AvatarImage } from "@radix-ui/react-avatar"
import { ChevronsUpDown, UserRound } from "lucide-react"

import { Avatar } from "@/libs/ui/components/avatar"
import { Button } from "@/libs/ui/components/button"
import { SidebarMenuButton } from "@/libs/ui/components/sidebar"

import { AccountMenu } from "./account-dropdown"

interface Props {
    variant?: "avatar" | "sidebar"
}

export function AccountButton({ variant }: Readonly<Props>) {
    const { user } = useUser()

    const emailAddress = user?.primaryEmailAddress?.emailAddress ?? undefined
    const imageUrl = user?.imageUrl ?? undefined
    const username = user?.username ?? undefined

    if (variant === "sidebar") {
        return (
            <AccountMenu>
                <SidebarMenuButton
                    size="lg"
                    className="data-[state=open]:bg-sidebar-accent data-[state=open]:text-sidebar-accent-foreground
                        cursor-pointer"
                >
                    <Avatar className="size-8">
                        <AvatarImage
                            src={imageUrl}
                            alt={username}
                        />
                        <AvatarFallback>
                            <UserRound size={16} />
                        </AvatarFallback>
                    </Avatar>
                    <div className="grid flex-1 text-left text-sm leading-tight tracking-tight">
                        <span className="truncate font-medium">{username}</span>
                        <span className="text-muted-foreground truncate text-xs">{emailAddress}</span>
                    </div>
                    <ChevronsUpDown className="ml-auto size-4" />
                </SidebarMenuButton>
            </AccountMenu>
        )
    }

    if (variant === "avatar") {
        return (
            <AccountMenu>
                <Button
                    size="avatar"
                    variant="ghost"
                    className="data-[state=open]:bg-accent"
                >
                    <Avatar className="size-8">
                        <AvatarImage
                            src={imageUrl}
                            alt={username}
                        />
                        <AvatarFallback>
                            <UserRound size={16} />
                        </AvatarFallback>
                    </Avatar>
                </Button>
            </AccountMenu>
        )
    }

    return (
        <AccountMenu>
            <Button
                size="lg"
                variant="ghost"
                className="data-[state=open]:bg-accent data-[state=open]:text-accent-foreground h-12 cursor-pointer
                    !px-2"
            >
                <Avatar>
                    <AvatarImage
                        src={imageUrl}
                        alt={username}
                    />
                    <AvatarFallback>
                        <UserRound size={16} />
                    </AvatarFallback>
                </Avatar>
                <div className="grid flex-1 text-left text-sm leading-tight tracking-tight">
                    <span className="truncate font-medium">{username}</span>
                    <span className="text-muted-foreground truncate text-xs">{emailAddress}</span>
                </div>
                <ChevronsUpDown className="ml-auto size-4" />
            </Button>
        </AccountMenu>
    )
}
