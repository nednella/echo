import React from "react"

import { AuthProvider } from "@/providers/auth-provider"
import { EchoQueryClientProvider } from "@/providers/query-client-provider"

interface Props {
    children: React.ReactNode
}

export function Provider({ children }: Readonly<Props>) {
    return (
        <AuthProvider>
            <EchoQueryClientProvider>{children}</EchoQueryClientProvider>
        </AuthProvider>
    )
}
