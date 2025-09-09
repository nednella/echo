import { useEffect, useState } from "react"

import { EchoLogo } from "../components/echo-logo"
import { Page } from "../components/page"
import { AnimatePresence, motion } from "motion/react"

interface Props {
    isReady: boolean
    minimumLoadingTime: number // millisec
    onAnimationComplete: () => void
}

export function LoadingPage({ isReady, minimumLoadingTime, onAnimationComplete }: Readonly<Props>) {
    const [shouldExit, setShouldExit] = useState(false)
    const [minLoadingTimeReached, setMinLoadingTimeReached] = useState(false)

    useEffect(() => {
        const id = setTimeout(() => {
            setMinLoadingTimeReached(true)
        }, minimumLoadingTime)

        return () => clearTimeout(id)
    }, [minimumLoadingTime])

    useEffect(() => {
        if (isReady && minLoadingTimeReached) {
            setShouldExit(true)
        }
    }, [isReady, minLoadingTimeReached])

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
                        <motion.div
                            className="fixed inset-0 flex items-center justify-center"
                            initial={{ scale: 1 }}
                            exit={{ scale: 25 }}
                            transition={{ duration: 0.5, ease: [0.76, 0, 0.24, 1] }}
                        >
                            {/* Logo */}
                            <motion.div
                                initial={{ opacity: 0, scale: 0.9 }}
                                animate={{ opacity: 1, scale: 1.1 }}
                                exit={{ opacity: 0, transition: { duration: 0.2 } }}
                                transition={{ duration: minimumLoadingTime / 1000, ease: "easeInOut" }}
                            >
                                <EchoLogo size={80} />
                            </motion.div>

                            {/* Loading Indicator */}
                            <motion.div
                                className="absolute bottom-20"
                                initial={{ opacity: 0, y: 20 }}
                                animate={{ opacity: 1, y: -20 }}
                                transition={{ duration: 0.6, delay: 0.6 }}
                            >
                                <div className="h-1 w-32 rounded-full bg-white/20">
                                    <motion.div
                                        className="h-full rounded-full bg-white/60"
                                        initial={{ width: 0 }}
                                        animate={{ width: isReady ? "95%" : "60%" }}
                                        transition={{ duration: minimumLoadingTime / 1000, ease: "easeInOut" }}
                                    />
                                </div>
                            </motion.div>
                        </motion.div>
                    </>
                )}
            </AnimatePresence>
        </Page>
    )
}
