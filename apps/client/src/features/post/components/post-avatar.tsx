import { UserRound } from "lucide-react"

import type { schemas } from "@/libs/api/openapi-client"
import { Avatar, AvatarFallback, AvatarImage } from "@/libs/ui/components/avatar"
import { cn } from "@/libs/ui/utils"

type PostAvatarProps = Readonly<{
    author: schemas["SimplifiedProfile"]
    className?: string
}>

export function PostAvatar({ author, className }: PostAvatarProps) {
    return (
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
}
