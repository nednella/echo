import type { Clerk } from "@clerk/clerk-js"

declare global {
    var Clerk: Clerk

    interface CustomJwtSessionClaims {
        echo_id: string | null
        onboarded: boolean
    }
}
