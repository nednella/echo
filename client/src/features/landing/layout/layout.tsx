import React from "react"

import Page from "../../../components/page"

interface Props {
    children: React.ReactNode
}

export default function Layout({ children }: Readonly<Props>) {
    return (
        <Page className="flex items-center justify-center bg-gradient-to-br">
            <section className="w-full max-w-md">{children}</section>
        </Page>
    )
}
