import { createFileRoute, Outlet, redirect } from "@tanstack/react-router"

// (public)/auth layout component ensuring access only for unauthenticated users
export const Route = createFileRoute("/(public)/auth")({
    beforeLoad({ context }) {
        // Redirect to home if user is already authenticated
        if (context.auth.isSignedIn === true) {
            throw redirect({
                to: "/home",
                replace: true
            })
        }
    },
    component: RouteComponent
})

function RouteComponent() {
    return <Outlet />
}
