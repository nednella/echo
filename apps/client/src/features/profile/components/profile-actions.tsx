import { Link2, MoreHorizontal } from "lucide-react"

import { Button } from "@/libs/ui/components/button"
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuTrigger
} from "@/libs/ui/components/dropdown-menu"
import { copyCurrentUrl } from "@/utils/clipboard"

export function ProfileActions() {
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
                <DropdownMenuItem onSelect={() => copyCurrentUrl()}>
                    <Link2 />
                    Copy link to profile
                </DropdownMenuItem>
            </DropdownMenuContent>
        </DropdownMenu>
    )
}
