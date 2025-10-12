import { Outlet, createFileRoute, redirect } from "@tanstack/react-router"

import { isAuthenticated, isOnboarded } from "@/common/utils/auth"

// (onboarding) authentication & onboarded check
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
