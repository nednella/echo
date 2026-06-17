import { useEffect } from "react"

import type { MutationStatus } from "@tanstack/react-query"
import { AnimatePresence, motion } from "motion/react"

import { ErrorState } from "./error-state"
import { SetupState } from "./setup-state"
import { SuccessState } from "./success-state"

type OnboardingAnimationProps = Readonly<{
    status: MutationStatus
    onSuccessAutoContinueMs: number
    onSuccessContinue: () => void
    onErrorRetry: () => void
}>

export function OnboardingAnimation({
    status,
    onSuccessAutoContinueMs,
    onSuccessContinue,
    onErrorRetry
}: OnboardingAnimationProps) {
    useEffect(() => {
        if (status === "success") {
            const id = setTimeout(onSuccessContinue, onSuccessAutoContinueMs)
            return () => clearTimeout(id)
        }
    }, [status, onSuccessAutoContinueMs, onSuccessContinue])

    return (
        <motion.div
            initial={{ opacity: 0, scale: 0.98 }}
            animate={{ opacity: 1, scale: 1 }}
            transition={{ duration: 0.3 }}
            className="bg-card w-full max-w-md rounded-2xl border p-8 text-center shadow-sm"
        >
            <AnimatePresence mode="wait">
                {(status === "idle" || status === "pending") && <SetupState key="setup" />}
                {status === "success" && (
                    <SuccessState
                        key="success"
                        onContinue={onSuccessContinue}
                    />
                )}
                {status === "error" && (
                    <ErrorState
                        key="error"
                        onRetry={onErrorRetry}
                    />
                )}
            </AnimatePresence>
        </motion.div>
    )
}
