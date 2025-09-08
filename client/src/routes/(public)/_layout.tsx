import { isAuthenticated } from "../../utils/auth"
import { createFileRoute, Outlet, redirect } from "@tanstack/react-router"

// /(public)/ authentication check
export const Route = createFileRoute("/(public)")({
    beforeLoad({ context }) {
        if (isAuthenticated(context.auth)) {
            throw redirect({
                to: "/home",
                replace: true
            })
        }
    },
    component: () => <Outlet />
})
