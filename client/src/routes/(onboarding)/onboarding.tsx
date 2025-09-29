import { useUser } from "@clerk/clerk-react"
import { createFileRoute, redirect } from "@tanstack/react-router"

import { Page } from "@/libs/ui/page"
import { isAuthenticated, isOnboarded } from "@/utils/auth"

export const Route = createFileRoute("/(onboarding)/onboarding")({
    beforeLoad({ context }) {
        if (!isAuthenticated(context.auth)) {
            throw redirect({
                to: "/login",
                replace: true
            })
        }

        if (isOnboarded(context.auth)) {
            throw redirect({
                to: "/home",
                replace: true
            })
        }
    },
    component: OnboardingPage
})

function OnboardingPage() {
    const user = useUser()

    console.log(user)

    return (
        <Page>
            <p>onboarding page</p>
        </Page>
    )
}
