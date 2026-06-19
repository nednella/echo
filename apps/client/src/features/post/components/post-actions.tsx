import { useUser } from "@clerk/clerk-react"
import { MoreHorizontal, Trash2 } from "lucide-react"

import type { schemas } from "@/libs/api/openapi-client"
import { Button } from "@/libs/ui/components/button"
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuTrigger
} from "@/libs/ui/components/dropdown-menu"
import { useDeletePostDialog } from "@/stores/delete-post-dialog.store"

type PostActionsProps = Readonly<{ post: schemas["Post"] }>

export function PostActions({ post }: PostActionsProps) {
    const { user } = useUser()
    const { onOpen: openDeletePostDialog } = useDeletePostDialog()

    const isOwn = post.author.username === user?.username

    if (!isOwn) return

    return (
        <DropdownMenu>
            <DropdownMenuTrigger asChild>
                <Button
                    variant="ghost"
                    size="icon-sm"
                    rounded
                    className="text-muted-foreground -mt-1 -mr-2 shrink-0"
                    aria-label="Post options"
                >
                    <MoreHorizontal />
                </Button>
            </DropdownMenuTrigger>
            <DropdownMenuContent align="end">
                <DropdownMenuItem
                    variant="destructive"
                    onSelect={() => openDeletePostDialog(post.id)}
                >
                    <Trash2 />
                    Delete
                </DropdownMenuItem>
            </DropdownMenuContent>
        </DropdownMenu>
    )
}
