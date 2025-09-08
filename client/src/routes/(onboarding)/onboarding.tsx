import { useUser } from "@clerk/clerk-react"
import { createFileRoute, redirect } from "@tanstack/react-router"

export const Route = createFileRoute("/(onboarding)/onboarding")({
    beforeLoad({ context }) {
        // Redirect to login if user is not authenticated
        if (context.auth.isSignedIn === false) {
            throw redirect({
                to: "/login",
                replace: true
            })
        }

        // Redirect to home if onboarding is completed
        if (context.auth.sessionClaims?.onboarded === true) {
            throw redirect({
                to: "/home",
                replace: true
            })
        }
    },
    component: RouteComponent
})

function RouteComponent() {
    const user = useUser()

    console.log(user)

    return <div>Hello "/(onboarding)/onboarding"!</div>
}
