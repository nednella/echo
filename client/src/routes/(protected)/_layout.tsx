import { createFileRoute, Outlet, redirect } from "@tanstack/react-router"

// (protected)/ layout component ensuring user authentication and onboarding status
export const Route = createFileRoute("/(protected)")({
    beforeLoad({ context, location }) {
        // Redirect to login if user is not authenticated
        if (context.auth.isSignedIn === false) {
            throw redirect({
                to: "/auth/login",
                replace: true,
                search: {
                    redirect: location.href // Preserve intended location for post-auth redirect
                }
            })
        }
    },
    component: Layout
})

function Layout() {
    return <Outlet />
}
