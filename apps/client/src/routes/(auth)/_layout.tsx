import { Outlet, createFileRoute, redirect } from "@tanstack/react-router"

import { Page } from "@/libs/ui/components/page"
import { isAuthenticated } from "@/utils/auth"

/**
 * Routes nested within the `/(auth)` pathless route group should only be accessible
 * when the user **IS NOT** authenticated, otherwise the user should be
 * redirected accordingly.
 *
 * docs: https://tanstack.com/router/v1/docs/framework/react/routing/routing-concepts#pathless-route-group-directories
 *
 * note: this should not be possible using pathless route groups, but it is: https://github.com/TanStack/router/discussions/5530
 */
export const Route = createFileRoute("/(auth)")({
    beforeLoad({ context: { auth } }) {
        if (isAuthenticated(auth)) {
            throw redirect({
                to: "/home",
                replace: true
            })
        }
    },
    component: AuthLayout
})

function AuthLayout() {
    return (
        <Page
            center
            landingGradient
        >
            <Outlet />
        </Page>
    )
}
