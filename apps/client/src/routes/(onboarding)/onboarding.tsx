import { useMutation } from "@tanstack/react-query"
import { createFileRoute, redirect } from "@tanstack/react-router"

import { onboardingMutationOptions } from "@/features/onboarding/api/options"
import { useEffectOnce } from "@/hooks/effect"
import { Page } from "@/libs/ui/page"
import { isAuthenticated, isOnboarded } from "@/utils/auth"

// (onboarding) authentication & onboarded check
export const Route = createFileRoute("/(onboarding)/onboarding")({
    beforeLoad({ context: { auth } }) {
        if (!isAuthenticated(auth)) {
            throw redirect({
                to: "/login",
                replace: true
            })
        }
        if (isOnboarded(auth)) {
            throw redirect({
                to: "/home",
                replace: true
            })
        }
    },
    component: OnboardingPage
})

function OnboardingPage() {
    const { auth } = Route.useRouteContext()
    const navigate = Route.useNavigate()
    const onboarding = useMutation(onboardingMutationOptions())

    useEffectOnce(() => {
        onboarding.mutate()
    })

    /**
     * Token should be force refreshed after onboarding completes, otherwise
     * the user will be redirected back to /onboarding due to missing claims.
     */
    const handleOnboardingComplete = async () => {
        await auth.getToken({ skipCache: true })
        navigate({ to: "/home", replace: true })
    }

    return <Page className="flex items-center justify-center">{/* TODO: loading animation component */}</Page>
}
