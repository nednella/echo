import { useSuspenseQuery } from "@tanstack/react-query"
import { createFileRoute } from "@tanstack/react-router"

import { PostList } from "@/features/post/components/post-list"
import { profileFeedInfiniteQueryOptions, profileQueryOptions } from "@/features/profile/api/options"

export const Route = createFileRoute("/(app)/profile/$username/replies")({
    loader: async ({ context: { queryClient }, params: { username } }) => {
        const profile = await queryClient.ensureQueryData(profileQueryOptions(username))
        await queryClient.ensureInfiniteQueryData(profileFeedInfiniteQueryOptions(profile.id, "replies"))
    },
    component: ProfileRepliesTab
})

function ProfileRepliesTab() {
    const { username } = Route.useParams()
    const { data: profile } = useSuspenseQuery(profileQueryOptions(username))

    return (
        <PostList
            options={profileFeedInfiniteQueryOptions(profile.id, "replies")}
            emptyMessage="No replies yet."
        />
    )
}
