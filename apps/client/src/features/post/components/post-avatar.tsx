import { Link } from "@tanstack/react-router"
import { UserRound } from "lucide-react"

import type { schemas } from "@/libs/api/openapi-client"
import { Avatar, AvatarFallback, AvatarImage } from "@/libs/ui/components/avatar"
import { cn } from "@/libs/ui/utils"

type PostAvatarProps = Readonly<{
    author: schemas["SimplifiedProfile"]
    interactive?: boolean
    className?: string
}>

export function PostAvatar({ author, interactive = true, className }: PostAvatarProps) {
    const avatar = (
        <Avatar className={cn("size-10 shrink-0", className)}>
            <AvatarImage
                src={author.image_url}
                alt={author.username}
            />
            <AvatarFallback>
                <UserRound className="size-1/2" />
            </AvatarFallback>
        </Avatar>
    )

    if (!interactive) return avatar

    return (
        <Link
            to="/profile/$username"
            params={{ username: author.username }}
            className="flex shrink-0 self-start transition-opacity hover:opacity-80"
        >
            {avatar}
        </Link>
    )
}
