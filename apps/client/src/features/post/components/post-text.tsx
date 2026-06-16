import type { ReactNode } from "react"

import { Link } from "@tanstack/react-router"

import type { schemas } from "@/libs/api/openapi-client"
import { parseEntities } from "@/libs/twitter-text/parse-entities"
import { cn } from "@/libs/ui/utils"

type Entities = schemas["PostEntities"]
type Kind = "mention" | "hashtag" | "url"

const ENTITY_CLASS = "text-echo-500 hover:underline"

function ordered(entities: Entities) {
    return [
        ...entities.mentions.map((entity) => ({ ...entity, kind: "mention" as Kind })),
        ...entities.hashtags.map((entity) => ({ ...entity, kind: "hashtag" as Kind })),
        ...entities.urls.map((entity) => ({ ...entity, kind: "url" as Kind }))
    ].toSorted((a, b) => a.start - b.start)
}

function withProtocol(url: string) {
    return /^https?:\/\//i.test(url) ? url : `https://${url}`
}

export function PostText({
    text,
    entities,
    interactive = true
}: Readonly<{ text: string; entities?: Entities; interactive?: boolean }>) {
    const nodes: ReactNode[] = []
    let cursor = 0

    for (const [index, entity] of ordered(entities ?? parseEntities(text)).entries()) {
        if (entity.start < cursor) continue
        if (entity.start > cursor) nodes.push(text.slice(cursor, entity.start))

        const display = text.slice(entity.start, entity.end)

        if (interactive && entity.kind === "mention") {
            nodes.push(
                <Link
                    key={index}
                    to="/profile/$username"
                    params={{ username: entity.text }}
                    className={ENTITY_CLASS}
                >
                    {display}
                </Link>
            )
        } else if (interactive && entity.kind === "url") {
            nodes.push(
                <a
                    key={index}
                    href={withProtocol(entity.text)}
                    target="_blank"
                    rel="noopener noreferrer nofollow"
                    className={ENTITY_CLASS}
                >
                    {display}
                </a>
            )
        } else {
            nodes.push(
                <span
                    key={index}
                    className={cn(ENTITY_CLASS, interactive && "cursor-pointer")}
                >
                    {display}
                </span>
            )
        }

        cursor = entity.end
    }

    if (cursor < text.length) nodes.push(text.slice(cursor))

    return nodes
}
