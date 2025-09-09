import React from "react"

import { Page } from "../../../components/page"
import { BackToHome } from "../components/back-to-home"
import { motion } from "motion/react"

interface Props {
    children: React.ReactNode
}

export function Layout({ children }: Readonly<Props>) {
    return (
        <Page className="to-echo-teal from-echo-navy flex items-center justify-center bg-gradient-to-br">
            <motion.div
                initial={{ y: 20, opacity: 0 }}
                animate={{ y: 0, opacity: 1 }}
                transition={{ duration: 0.6, delay: 0.2 }}
            >
                <BackToHome />

                {/* Auth component */}
                <motion.div
                    initial={{ scale: 0.95, opacity: 0 }}
                    animate={{ scale: 1, opacity: 1 }}
                    transition={{ duration: 0.6, delay: 0.2 }}
                >
                    {children}
                </motion.div>
            </motion.div>
        </Page>
    )
}
