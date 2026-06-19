import { Link2, MoreHorizontal } from "lucide-react"

import { Button } from "@/libs/ui/components/button"
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuTrigger
} from "@/libs/ui/components/dropdown-menu"
import { copyLink } from "@/utils/clipboard"

type ProfileActionsProps = Readonly<{ username: string }>

export function ProfileActions({ username }: ProfileActionsProps) {
    return (
        <DropdownMenu>
            <DropdownMenuTrigger asChild>
                <Button
                    variant="outline"
                    size="icon"
                    rounded
                    aria-label="Profile options"
                >
                    <MoreHorizontal />
                </Button>
            </DropdownMenuTrigger>
            <DropdownMenuContent align="end">
                <DropdownMenuItem onSelect={() => copyLink(`/profile/${username}`)}>
                    <Link2 />
                    Copy link to profile
                </DropdownMenuItem>
            </DropdownMenuContent>
        </DropdownMenu>
    )
}
