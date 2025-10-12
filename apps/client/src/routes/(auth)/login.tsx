import { SignIn } from "@clerk/clerk-react"
import { createFileRoute } from "@tanstack/react-router"

import { AuthFormWrapper } from "@/features/auth/components/auth-form-wrapper"

export const Route = createFileRoute("/(auth)/login")({
    component: LoginPage
})

function LoginPage() {
    return (
        <AuthFormWrapper>
            <SignIn />
        </AuthFormWrapper>
    )
}
