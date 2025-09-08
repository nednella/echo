import React from "react"

import AuthProvider from "../providers/AuthProvider"

interface Props {
    children: React.ReactNode
}

export default function Provider({ children }: Props) {
    return <AuthProvider>{children}</AuthProvider>
}
