import { createFileRoute, Outlet, redirect } from "@tanstack/react-router"

// /(public)/ layout component ensuring access only for unauthenticated users
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
