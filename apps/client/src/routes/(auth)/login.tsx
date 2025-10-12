import { SignIn } from "@clerk/clerk-react"
import { createFileRoute } from "@tanstack/react-router"

import { AuthFormWrapper } from "@/features/auth/components/auth-form-wrapper"
import { Page } from "@/libs/ui/page"

export const Route = createFileRoute("/(auth)/login")({
    component: LoginPage
})

function LoginPage() {
    return (
        <Page
            center
            landingGradient
        >
            <AuthFormWrapper>
                <SignIn />
            </AuthFormWrapper>
        </Page>
    )
}
