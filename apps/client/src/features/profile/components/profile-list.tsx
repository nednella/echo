import { useEffect, useRef } from "react"

import { useInfiniteQuery } from "@tanstack/react-query"

import { Spinner } from "@/components/spinner"
import { ProfileListItem } from "@/features/profile/components/profile-list-item"
import type { schemas } from "@/libs/api/openapi-client"
import type { PagedInfiniteQueryOptions } from "@/utils/pagination"

type ProfileListProps = Readonly<{
    options: PagedInfiniteQueryOptions<schemas["SimplifiedProfile"]>
    emptyMessage: string
    scrollRootRef?: React.RefObject<HTMLElement | null>
}>

export function ProfileList({ options, emptyMessage, scrollRootRef }: ProfileListProps) {
    const { data, fetchNextPage, hasNextPage, isFetchingNextPage, isLoading } = useInfiniteQuery(options)
    const profiles = data?.pages.flatMap((page) => page.items) ?? []

    const sentinelReference = useRef<HTMLDivElement>(null)
    useEffect(() => {
        const element = sentinelReference.current
        if (!element || !hasNextPage) return

        const observer = new IntersectionObserver(
            (entries) => {
                if (entries[0]?.isIntersecting) fetchNextPage()
            },
            // eslint-disable-next-line unicorn/no-null
            { root: scrollRootRef?.current ?? null, rootMargin: "200px" }
        )
        observer.observe(element)
        return () => observer.disconnect()
    }, [hasNextPage, fetchNextPage, scrollRootRef])

    if (isLoading) {
        return (
            <div className="flex justify-center py-16">
                <Spinner />
            </div>
        )
    }

    if (profiles.length === 0) {
        return <p className="text-muted-foreground py-16 text-center text-sm">{emptyMessage}</p>
    }

    return (
        <div>
            {profiles.map((profile) => (
                <ProfileListItem
                    key={profile.id}
                    profile={profile}
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
