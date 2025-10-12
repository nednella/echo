import { SignUp } from "@clerk/clerk-react"
import { createFileRoute } from "@tanstack/react-router"

export const Route = createFileRoute("/(auth)/register")({
    component: RegisterPage
})

function RegisterPage() {
    return <SignUp />
}
