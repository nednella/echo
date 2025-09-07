import { createFileRoute, Outlet, redirect } from "@tanstack/react-router"

// /(protected)/ layout component ensuring user authentication and onboarding status
export const Route = createFileRoute("/(protected)")({
    beforeLoad({ context, location }) {
        // Redirect to login if user is not authenticated
        if (context.auth.isSignedIn === false) {
            throw redirect({
                to: "/login",
                replace: true,
                search: {
                    redirect: location.href // Preserve intended location for post-auth redirect
                }
            })
        }

        // Redirect to onboarding if onboarding is not completed
        if (!context.auth.sessionClaims?.metadata.onboardingComplete) {
            throw redirect({
                to: "/onboarding",
                replace: true
            })
        }
    },
    component: Layout
})

function Layout() {
    return <Outlet />
}
