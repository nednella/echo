import { useSuspenseQuery } from "@tanstack/react-query"
import { Outlet, createFileRoute, notFound } from "@tanstack/react-router"

import { AppRoute } from "@/components/page/app-route"
import { AppRouteNotFound } from "@/components/page/app-route-not-found"
import { FeedContainer } from "@/features/feed/components/feed-container"
import { profileQueryOptions } from "@/features/profile/api/options"
import { ProfileHeader } from "@/features/profile/components/profile-header"
import { ProfileTabs } from "@/features/profile/components/profile-tabs"
import { ApiException } from "@/libs/api/exception"

export const Route = createFileRoute("/(app)/profile/$username")({
    loader: async ({ context: { queryClient }, params: { username } }) => {
        try {
            await queryClient.ensureQueryData(profileQueryOptions(username))
        } catch (error) {
            if (error instanceof ApiException && error.status === 404) throw notFound()
            throw error
        }
    },
    component: ProfileLayout,
    notFoundComponent: () => <AppRouteNotFound path="profile" />
})

function ProfileLayout() {
    const { username } = Route.useParams()
    const { data: profile } = useSuspenseQuery(profileQueryOptions(username))

    return (
        <AppRoute>
            <FeedContainer>
                <ProfileHeader profile={profile} />
                <ProfileTabs username={username} />
                <Outlet />
            </FeedContainer>
        </AppRoute>
    )
}
