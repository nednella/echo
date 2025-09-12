import React from "react"

import { MotionContainer } from "@/components/ui/container"
import { Page } from "@/components/ui/page"

import { BackToHome } from "../components/back-to-home"

interface Props {
    children: React.ReactNode
}

export function Layout({ children }: Readonly<Props>) {
    return (
        <Page className="to-echo-teal from-echo-navy flex items-center justify-center bg-gradient-to-br">
            <MotionContainer
                className="w-fit"
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
                transition={{ duration: 0.6, delay: 0.2 }}
            >
                <BackToHome />
                <MotionContainer
                    initial={{ y: 20, scale: 0.95, opacity: 0 }}
                    animate={{ y: 0, scale: 1, opacity: 1 }}
                    transition={{ duration: 0.6, delay: 0.2 }}
                >
                    {children}
                </MotionContainer>
            </MotionContainer>
        </Page>
    )
}
