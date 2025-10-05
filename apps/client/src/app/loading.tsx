import { useEffect, useState } from "react"

import { AnimatePresence, motion } from "motion/react"

import { MotionContainer } from "@/libs/ui/container"
import { EchoLogo } from "@/libs/ui/echo-logo"
import { Page } from "@/libs/ui/page"

interface Props {
    isReady: boolean
    minimumLoadingTimeMs: number
    onAnimationComplete: () => void
}

export function LoadingPage({ isReady, minimumLoadingTimeMs, onAnimationComplete }: Readonly<Props>) {
    const [shouldExit, setShouldExit] = useState(false)
    const [minLoadingTimeReached, setMinLoadingTimeReached] = useState(false)

    // TODO: refactor into a single useEffect
    useEffect(() => {
        const id = setTimeout(() => {
            setMinLoadingTimeReached(true)
        }, minimumLoadingTimeMs)

        return () => clearTimeout(id)
    }, [minimumLoadingTimeMs])

    useEffect(() => {
        if (isReady && minLoadingTimeReached) {
            setShouldExit(true)
        }
    }, [isReady, minLoadingTimeReached])

    // TODO: refactor animation
    return (
        <Page className="p-0">
            <AnimatePresence onExitComplete={onAnimationComplete}>
                {!shouldExit && (
                    <>
                        {/* Background gradient */}
                        <motion.div
                            className="to-echo-teal from-echo-navy absolute inset-0 bg-gradient-to-br"
                            initial={{ opacity: 1 }}
                            exit={{ opacity: 0 }}
                            transition={{ delay: 0.2 }}
                        />
                        {/* Scale transition */}
                        <MotionContainer
                            className="fixed inset-0 flex items-center justify-center"
                            initial={{ scale: 1 }}
                            exit={{ scale: 25 }}
                            transition={{ duration: 0.5, ease: [0.76, 0, 0.24, 1] }}
                        >
                            {/* Logo */}
                            <MotionContainer
                                className="w-fit"
                                initial={{ opacity: 0, scale: 0.8 }}
                                animate={{ opacity: 1, scale: 1 }}
                                exit={{ opacity: 0, transition: { duration: 0.2 } }}
                                transition={{ duration: minimumLoadingTimeMs / 1000, ease: "easeInOut" }}
                            >
                                <EchoLogo
                                    size={96}
                                    variant="light-gradient"
                                />
                            </MotionContainer>

                            {/* Loading Indicator */}
                            <MotionContainer
                                className="absolute bottom-20 w-fit"
                                initial={{ opacity: 0, y: 20 }}
                                animate={{ opacity: 1, y: -20 }}
                                transition={{ duration: 0.6, delay: 0.6 }}
                            >
                                <div className="h-1 w-32 rounded-full bg-white/20">
                                    <motion.div
                                        className="h-full rounded-full bg-white/60"
                                        initial={{ width: 0 }}
                                        animate={{ width: isReady ? "95%" : "60%" }}
                                        transition={{ duration: minimumLoadingTimeMs / 1000, ease: "easeInOut" }}
                                    />
                                </div>
                            </MotionContainer>
                        </MotionContainer>
                    </>
                )}
            </AnimatePresence>
        </Page>
    )
}
