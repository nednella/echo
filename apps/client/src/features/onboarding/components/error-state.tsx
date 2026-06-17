import { RotateCw, TriangleAlert } from "lucide-react"
import { motion } from "motion/react"

import { Button } from "@/libs/ui/components/button"

const fade = {
    initial: { opacity: 0, y: 6 },
    animate: { opacity: 1, y: 0 },
    exit: { opacity: 0, y: -6 },
    transition: { duration: 0.25 }
}

type ErrorStateProps = Readonly<{ onRetry: () => void }>

export function ErrorState({ onRetry }: ErrorStateProps) {
    return (
        <motion.div {...fade}>
            <motion.div
                initial={{ scale: 0.6, opacity: 0 }}
                animate={{ scale: 1, opacity: 1 }}
                transition={{ type: "spring", stiffness: 320, damping: 18 }}
                className={`bg-destructive ring-destructive/15 mx-auto grid size-16 place-items-center rounded-full
                    text-white ring-8`}
            >
                <TriangleAlert size={28} />
            </motion.div>
            <h1 className="mt-5 text-xl font-semibold tracking-tight">Something went wrong</h1>
            <p className="text-muted-foreground mt-2 text-sm">
                {"We couldn't finish setting up your account. Give it another go."}
            </p>
            <Button
                full
                rounded
                variant="custom"
                onClick={onRetry}
                className="bg-echo-600 hover:bg-echo-700 mt-6 font-semibold text-white"
            >
                <RotateCw />
                Try again
            </Button>
        </motion.div>
    )
}
