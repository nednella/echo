import { createFileRoute, Outlet, redirect } from "@tanstack/react-router"

/**
 * Wrapper for child routes to ensure the user is authenticated before rendering the page.
 */

export const Route = createFileRoute("/_auth")({
    beforeLoad({ context, location }) {
        if (!context.auth.isSignedIn) {
            throw redirect({
                to: "/",
                search: {
                    redirect: location.href
                }
            })
        }
    },
    component: AuthWrapper
})

function AuthWrapper() {
    return <Outlet />
}
