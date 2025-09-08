import { SignUp } from "@clerk/clerk-react"
import { createFileRoute } from "@tanstack/react-router"

export const Route = createFileRoute("/(public)/(auth)/register")({
    component: () => <SignUp />
})
