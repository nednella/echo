import { useUser } from "@clerk/clerk-react"
import { UserRound } from "lucide-react"

import { Avatar, AvatarFallback, AvatarImage } from "@/libs/ui/components/avatar"

export function AccountAvatar({ className }: Readonly<{ className?: string }>) {
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
