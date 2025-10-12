import { Outlet, createFileRoute, redirect } from "@tanstack/react-router"

import { isAuthenticated, isOnboarded } from "@/common/utils/auth"
import { Container } from "@/libs/ui/components/container"
import { Page } from "@/libs/ui/components/page"

/**
 * Routes nested within the `/(app)` pathless layout should only be accessible
 * when the user **IS BOTH** authenticated and onboarded, otherwise the user should
 * be redirected accordingly.
 */
export const Route = createFileRoute("/(app)")({
    beforeLoad({ context: { auth }, location }) {
        if (!isAuthenticated(auth)) {
            throw redirect({
                to: "/login",
                replace: true,
                search: {
                    redirect: location.href
                }
            })
        }
        if (!isOnboarded(auth)) {
            throw redirect({
                to: "/onboarding",
                replace: true
            })
        }
    },
    component: AppLayout
})

function AppLayout() {
    return (
        <Page
            pad
            className="grid grid-cols-[max-content_1fr] grid-rows-1 gap-2"
        >
            <div className="flex w-60 flex-col justify-between rounded-md">
                <div>Sidebar Header</div>
                <div>Sidebar Nav</div>
                <div>Account Button</div>
            </div>
            <Container className="bg-card relative flex flex-col overflow-y-auto rounded-md">
                <Outlet />
            </Container>
        </Page>
    )
}
