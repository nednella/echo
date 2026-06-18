import { useUser } from "@clerk/clerk-react"
import { UserRound } from "lucide-react"

import { Avatar, AvatarFallback, AvatarImage } from "@/libs/ui/components/avatar"

type AccountAvatarProps = Readonly<{ className?: string }>

export function AccountAvatar({ className }: AccountAvatarProps) {
    const { user } = useUser()

    return (
        <Avatar className={className}>
            <AvatarImage
                src={user?.imageUrl}
                alt={user?.username ?? undefined}
            />
            <AvatarFallback>
                <UserRound size={16} />
            </AvatarFallback>
        </Avatar>
    )
}
