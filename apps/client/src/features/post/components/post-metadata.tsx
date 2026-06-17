import type { schemas } from "@/libs/api/openapi-client"
import { relativeTime } from "@/utils/datetime"

type PostMetadataProps = Readonly<{
    author: schemas["SimplifiedProfile"]
    createdAt: string
}>

export function PostMetadata({ author, createdAt }: PostMetadataProps) {
    return (
        <div className="flex items-baseline gap-1.5 text-sm">
            <span className="truncate font-semibold">{author.name ?? author.username}</span>
            <span className="text-muted-foreground truncate">@{author.username}</span>
            <span className="text-muted-foreground">·</span>
            <span className="text-muted-foreground shrink-0">{relativeTime(createdAt)}</span>
        </div>
    )
}
