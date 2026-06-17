import { useEffect, useRef } from "react"

import { useInfiniteQuery } from "@tanstack/react-query"

import { Spinner } from "@/components/spinner"
import { type FeedType, feedInfiniteQueryOptions } from "@/features/feed/api/options"
import { Post } from "@/features/post/components/post"

type FeedProps = Readonly<{ feed: FeedType }>

export function Feed({ feed }: FeedProps) {
    const { data, fetchNextPage, hasNextPage, isFetchingNextPage } = useInfiniteQuery(feedInfiniteQueryOptions(feed))
    const posts = data?.pages.flatMap((page) => page.items) ?? []

    const sentinelReference = useRef<HTMLDivElement>(null)
    useEffect(() => {
        const element = sentinelReference.current
        if (!element || !hasNextPage) return

        const observer = new IntersectionObserver(
            (entries) => {
                if (entries[0]?.isIntersecting) fetchNextPage()
            },
            { rootMargin: "600px" }
        )
        observer.observe(element)
        return () => observer.disconnect()
    }, [hasNextPage, fetchNextPage])

    if (posts.length === 0) {
        return <p className="text-muted-foreground py-16 text-center text-sm">No posts here yet.</p>
    }

    return (
        <div>
            {posts.map((post) => (
                <Post
                    key={post.id}
                    post={post}
                />
            ))}
            <div ref={sentinelReference} />
            {isFetchingNextPage && (
                <div className="flex justify-center py-6">
                    <Spinner />
                </div>
            )}
        </div>
    )
}
