import twitterText from "twitter-text"

import type { schemas } from "@/libs/api/openapi-client"

type Entity = schemas["PostEntity"]

export function parseEntities(text: string): schemas["PostEntities"] {
    const mentions: Entity[] = []
    const hashtags: Entity[] = []
    const urls: Entity[] = []

    for (const entity of twitterText.extractEntitiesWithIndices(text)) {
        const [start, end] = entity.indices
        if ("screenName" in entity) mentions.push({ start, end, text: entity.screenName })
        else if ("hashtag" in entity) hashtags.push({ start, end, text: entity.hashtag })
        else if ("url" in entity) urls.push({ start, end, text: entity.url })
    }

    return { mentions, hashtags, urls }
}
