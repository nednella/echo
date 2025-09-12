import React from "react"

import { Container } from "@/components/ui/container"
import { Page } from "@/components/ui/page"

interface Props {
    children: React.ReactNode
}

export function Layout({ children }: Readonly<Props>) {
    return (
        <Page className="to-echo-teal from-echo-navy flex items-center justify-center bg-gradient-to-br">
            <Container className="max-w-md">{children}</Container>
        </Page>
    )
}
