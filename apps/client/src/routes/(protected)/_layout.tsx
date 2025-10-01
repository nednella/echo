import { Outlet, createFileRoute, redirect } from "@tanstack/react-router"

import { isAuthenticated, isOnboarded } from "@/utils/auth"

// /(protected)/ authentication/onboarded check
export const Route = createFileRoute("/(protected)")({
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
