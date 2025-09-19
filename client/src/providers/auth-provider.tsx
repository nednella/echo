import React from "react"

import { ClerkProvider } from "@clerk/clerk-react"

const CLERK_PUBLISHABLE_KEY = import.meta.env.VITE_CLERK_PUBLISHABLE_KEY

if (!CLERK_PUBLISHABLE_KEY) {
    throw new Error("Clerk Publishable Key missing from .env file!")
}

interface Props {
    children: React.ReactNode
}

export function AuthProvider({ children }: Readonly<Props>) {
    return <ClerkProvider publishableKey={CLERK_PUBLISHABLE_KEY}>{children}</ClerkProvider>
}
