import { SignUp } from "@clerk/clerk-react"
import { createFileRoute } from "@tanstack/react-router"

import { AuthFormWrapper } from "@/features/auth/components/auth-form-wrapper"
import { Page } from "@/libs/ui/page"

export const Route = createFileRoute("/(auth)/register")({
    component: RegisterPage
})

function RegisterPage() {
    return (
        <Page
            center
            landingGradient
        >
            <AuthFormWrapper>
                <SignUp />
            </AuthFormWrapper>
        </Page>
    )
}
