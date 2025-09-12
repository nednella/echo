import React from "react"

import { AuthProvider } from "@/providers/AuthProvider"

interface Props {
    children: React.ReactNode
}

export function Provider({ children }: Readonly<Props>) {
    return <AuthProvider>{children}</AuthProvider>
}
