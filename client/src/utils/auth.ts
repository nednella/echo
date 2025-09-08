import type { useAuth } from "@clerk/clerk-react"

export function isAuthenticated(auth: ReturnType<typeof useAuth>) {
    return auth.isSignedIn
}

export function isOnboarded(auth: ReturnType<typeof useAuth>) {
    return auth.sessionClaims?.onboarded
}
