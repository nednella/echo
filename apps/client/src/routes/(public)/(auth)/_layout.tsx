import { Outlet, createFileRoute } from "@tanstack/react-router"

import { MotionContainer } from "@/common/components/container"
import { Page } from "@/common/components/page"
import { BackToHome } from "@/features/auth/components/back-to-home"

// (auth) layout
export const Route = createFileRoute("/(public)/(auth)")({
    component: AuthLayout
})

function AuthLayout() {
    return (
        <Page
            center
            landingGradient
        >
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
                    <Outlet />
                </MotionContainer>
            </MotionContainer>
        </Page>
    )
}
