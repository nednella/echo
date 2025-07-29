import { useUser } from "@clerk/clerk-react"
import { createFileRoute, redirect } from "@tanstack/react-router"

export const Route = createFileRoute("/(onboarding)/onboarding")({
    beforeLoad({ context }) {
        // redirect user if onboardingComplete TRUE
        if (context.auth.sessionClaims?.metadata.onboardingComplete === true) {
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
