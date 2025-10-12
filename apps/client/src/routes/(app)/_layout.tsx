import { Outlet, createFileRoute, redirect } from "@tanstack/react-router"

import { isAuthenticated, isOnboarded } from "@/common/utils/auth"

// (app) authentication & onboarded check
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
