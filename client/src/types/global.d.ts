declare global {
    interface CustomJwtSessionClaims {
        echo_id: string | null
        onboarded: boolean
    }
}
