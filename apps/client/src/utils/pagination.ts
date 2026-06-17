import { type QueryKey, infiniteQueryOptions } from "@tanstack/react-query"

export const PAGE_SIZE = 20

type Page<T> = {
    offset: number
    limit: number
    total: number
    items: T[]
}

export type PagedInfiniteQueryOptions<T> = ReturnType<typeof pagedInfiniteQueryOptions<T>>

export const pagedInfiniteQueryOptions = <T>(queryKey: QueryKey, fetchPage: (offset: number) => Promise<Page<T>>) =>
    infiniteQueryOptions({
        queryKey,
        queryFn: ({ pageParam }) => fetchPage(pageParam),
        initialPageParam: 0,
        getNextPageParam: (last) => {
            const next = last.offset + last.limit
            return next < last.total ? next : undefined
        }
    })
