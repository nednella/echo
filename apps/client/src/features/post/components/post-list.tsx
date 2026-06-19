import { useEffect, useRef } from "react"

import { useInfiniteQuery } from "@tanstack/react-query"

import { Spinner } from "@/components/spinner"
import { Post } from "@/features/post/components/post"
import type { schemas } from "@/libs/api/openapi-client"
import type { PagedInfiniteQueryOptions } from "@/utils/pagination"

type PostListProps = Readonly<{
    options: PagedInfiniteQueryOptions<schemas["Post"]>
    emptyMessage?: string
}>

export function PostList({ options, emptyMessage }: PostListProps) {
    const { data, fetchNextPage, hasNextPage, isFetchingNextPage } = useInfiniteQuery(options)
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
        return (
            <p className="text-muted-foreground py-16 text-center text-sm">
                {emptyMessage ?? "Nothing to see here yet."}
            </p>
        )
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
