import { createFileRoute, Outlet, redirect } from "@tanstack/react-router"

// /(public)/ authentication check
export const Route = createFileRoute("/(public)")({
    beforeLoad({ context }) {
        // Redirect to home if user is already authenticated
        if (context.auth.isSignedIn === true) {
            throw redirect({
                to: "/home",
                replace: true
            })
        }
    },
    component: () => <Outlet />
})
