import { Outlet, createFileRoute, redirect } from "@tanstack/react-router"

import { isAuthenticated, isOnboarded } from "@/common/utils/auth"

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
    component: () => <Outlet />
})
