import React from "react"

import { Page } from "../../../components/ui/page"

interface Props {
    children: React.ReactNode
}

export function Layout({ children }: Readonly<Props>) {
    return (
        <Page className="to-echo-teal from-echo-navy flex items-center justify-center bg-gradient-to-br">
            <section className="w-full max-w-md">{children}</section>
        </Page>
    )
}
