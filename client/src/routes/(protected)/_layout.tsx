import { createFileRoute, Outlet, redirect } from "@tanstack/react-router"

// /(protected)/ authentication check
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
        if (!context.auth.sessionClaims?.onboarded) {
            throw redirect({
                to: "/onboarding",
                replace: true
            })
        }
    },
    component: () => <Outlet />
})
