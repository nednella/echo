import { useEffect } from "react"

import type { MutationStatus } from "@tanstack/react-query"
import { AnimatePresence } from "motion/react"

import Spinner from "@/common/components/spinner"
import { Button } from "@/libs/ui/button"
import { MotionContainer } from "@/libs/ui/container"

interface Props {
    status: MutationStatus
    onSuccessAutoContinueMs: number
    onSuccessContinue: () => void
    onErrorRetry: () => void
}

export function OnboardingAnimation({
    status,
    onSuccessAutoContinueMs,
    onSuccessContinue,
    onErrorRetry
}: Readonly<Props>) {
    useEffect(() => {
        if (status === "success") {
            const id = setTimeout(onSuccessContinue, onSuccessAutoContinueMs)
            return () => clearTimeout(id)
        }
    }, [status, onSuccessAutoContinueMs, onSuccessContinue])

    return (
        <AnimatePresence mode="wait">
            {(status === "idle" || status === "pending") && (
                <MotionContainer className="flex w-fit flex-col items-center gap-4">
                    <Spinner />
                    <p>Just getting things set up for you, hold on!</p>
                </MotionContainer>
            )}

            {status === "success" && (
                <MotionContainer className="w-fit">
                    <p>Success!</p>
                </MotionContainer>
            )}

            {status === "error" && (
                <MotionContainer className="flex w-fit flex-col items-center gap-4">
                    <p>Something went wrong :(</p>
                    <Button onClick={onErrorRetry}>Retry</Button>
                </MotionContainer>
            )}
        </AnimatePresence>
    )
}
