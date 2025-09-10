import { isAuthenticated, isOnboarded } from "../../utils/auth"
import { createFileRoute, Outlet, redirect } from "@tanstack/react-router"

// /(protected)/ authentication/onboarding check
export const Route = createFileRoute("/(protected)")({
    beforeLoad({ context, location }) {
        if (!isAuthenticated(context.auth)) {
            throw redirect({
                to: "/login",
                replace: true,
                search: {
                    redirect: location.href
                }
            })
        }

        if (!isOnboarded(context.auth)) {
            throw redirect({
                to: "/onboarding",
                replace: true
            })
        }
    },
    component: () => <Outlet />
})
