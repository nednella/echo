import { UserRound } from "lucide-react"

import { Avatar, AvatarFallback, AvatarImage } from "@/libs/ui/components/avatar"

type UserAvatarProps = Readonly<{
    src?: string
    alt?: string
    className?: string
}>

export function UserAvatar({ src, alt, className }: UserAvatarProps) {
    return (
        <Avatar className={className}>
            <AvatarImage
                src={src}
                alt={alt}
            />
            <AvatarFallback>
                <UserRound className="size-1/2" />
            </AvatarFallback>
        </Avatar>
    )
}
