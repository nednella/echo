import { Outlet, createFileRoute, redirect } from "@tanstack/react-router"

import { isAuthenticated, isOnboarded } from "@/common/utils/auth"

/**
 * Routes nested within the `/(onboarding)` pathless layout should only be accessible
 * when the user **IS** authenticated, but **IS NOT** onboarded, otherwise the user
 * should be redirected accordingly.
 */
export const Route = createFileRoute("/(onboarding)")({
    beforeLoad({ context: { auth } }) {
        if (!isAuthenticated(auth)) {
            throw redirect({
                to: "/login",
                replace: true
            })
        }
        if (isOnboarded(auth)) {
            throw redirect({
                to: "/home",
                replace: true
            })
        }
    },
    component: () => <Outlet />
})
