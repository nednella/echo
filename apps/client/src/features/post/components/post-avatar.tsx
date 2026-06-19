import { Link } from "@tanstack/react-router"

import { UserAvatar } from "@/components/user-avatar"
import type { schemas } from "@/libs/api/openapi-client"
import { cn } from "@/libs/ui/utils"

type PostAvatarProps = Readonly<{
    author: schemas["SimplifiedProfile"]
    interactive?: boolean
    className?: string
}>

export function PostAvatar({ author, interactive = true, className }: PostAvatarProps) {
    const avatar = (
        <UserAvatar
            src={author.image_url}
            alt={author.username}
            className={cn("size-10 shrink-0", className)}
        />
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
