import type React from "react"

import { MotionContainer } from "@/libs/ui/components/container"

import { BackToHome } from "./back-to-home"

type AuthFormWrapperProps = Readonly<{
    children: React.ReactNode
}>

export function AuthFormWrapper({ children }: AuthFormWrapperProps) {
    return (
        <MotionContainer
            className="w-fit"
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            transition={{ duration: 0.6, delay: 0.2 }}
        >
            <BackToHome />
            <MotionContainer
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
                transition={{ duration: 0.6, delay: 0.2 }}
            >
                {children}
            </MotionContainer>
        </MotionContainer>
    )
}
