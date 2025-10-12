import { Outlet, createFileRoute, redirect } from "@tanstack/react-router"

import { isAuthenticated } from "@/common/utils/auth"

// (auth) authentication check
export const Route = createFileRoute("/(auth)")({
    beforeLoad({ context: { auth } }) {
        if (isAuthenticated(auth)) {
            throw redirect({
                to: "/home",
                replace: true
            })
        }
    },
    component: () => <Outlet />
})
