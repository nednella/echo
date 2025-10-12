import { Outlet, createFileRoute, redirect } from "@tanstack/react-router"

import { isAuthenticated } from "@/common/utils/auth"
import { Page } from "@/libs/ui/page"

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
