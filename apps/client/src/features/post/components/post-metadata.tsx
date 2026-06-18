import type { ReactNode } from "react"

import { Link } from "@tanstack/react-router"

import type { schemas } from "@/libs/api/openapi-client"
import { cn } from "@/libs/ui/utils"
import { relativeTime } from "@/utils/datetime"

type PostMetadataProps = Readonly<{
    author: schemas["SimplifiedProfile"]
    createdAt: string
    interactive?: boolean
}>

export function PostMetadata({ author, createdAt, interactive = true }: PostMetadataProps) {
    const profileLink = (text: ReactNode, className: string) =>
        interactive ? (
            <Link
                to="/profile/$username"
                params={{ username: author.username }}
                className={cn("hover:underline", className)}
            >
                {text}
            </Link>
        ) : (
            <span className={className}>{text}</span>
        )

    return (
        <div className="flex items-baseline gap-1.5 text-sm">
            {profileLink(author.name ?? author.username, "truncate font-semibold")}
            {profileLink(`@${author.username}`, "text-muted-foreground truncate")}
            <span className="text-muted-foreground">·</span>
            <span className="text-muted-foreground shrink-0">{relativeTime(createdAt)}</span>
        </div>
    )
}
