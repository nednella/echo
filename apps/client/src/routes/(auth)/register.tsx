import { SignUp } from "@clerk/clerk-react"
import { createFileRoute } from "@tanstack/react-router"

import { AuthFormWrapper } from "@/features/auth/components/auth-form-wrapper"

export const Route = createFileRoute("/(auth)/register")({
    component: RegisterPage
})

function RegisterPage() {
    return (
        <AuthFormWrapper>
            <SignUp />
        </AuthFormWrapper>
    )
}
