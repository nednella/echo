import { Outlet, createFileRoute, redirect } from "@tanstack/react-router"

import { isAuthenticated } from "@/common/utils/auth"
import { Page } from "@/libs/ui/components/page"

/**
 * Routes nested within the `/(auth)` pathless layout should only be accessible
 * when the user **IS NOT** authenticated, otherwise the user should be
 * redirected accordingly.
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
