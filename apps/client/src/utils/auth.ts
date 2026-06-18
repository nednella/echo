import type { useAuth } from "@clerk/clerk-react"

export const isAuthenticated = (auth: ReturnType<typeof useAuth>) => {
    return auth.isSignedIn
}

export const isOnboarded = (auth: ReturnType<typeof useAuth>) => {
    return auth.sessionClaims?.onboarded
}
