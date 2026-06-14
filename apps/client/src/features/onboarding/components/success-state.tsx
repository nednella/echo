import { Check } from "lucide-react"
import { motion } from "motion/react"

import { Button } from "@/libs/ui/components/button"

const fade = {
    initial: { opacity: 0, y: 6 },
    animate: { opacity: 1, y: 0 },
    exit: { opacity: 0, y: -6 },
    transition: { duration: 0.25 }
}

export function SuccessState({ onContinue }: Readonly<{ onContinue: () => void }>) {
    return (
        <motion.div {...fade}>
            <motion.div
                initial={{ scale: 0.6, opacity: 0 }}
                animate={{ scale: 1, opacity: 1 }}
                transition={{ type: "spring", stiffness: 320, damping: 18 }}
                className={`mx-auto grid size-16 place-items-center rounded-full bg-[#22c55e] text-white ring-8
                    ring-[#22c55e]/15`}
            >
                <Check
                    size={28}
                    strokeWidth={3}
                />
            </motion.div>
            <h1 className="mt-5 text-xl font-semibold tracking-tight">{"You're all set!"}</h1>
            <p className="text-muted-foreground mt-2 text-sm">{"Welcome to echo. Let's get you to your feed."}</p>
            <Button
                full
                rounded
                variant="custom"
                onClick={onContinue}
                className="from-echo-400 to-echo-600 mt-6 bg-gradient-to-br font-semibold text-white
                    hover:brightness-110"
            >
                Continue
            </Button>
        </motion.div>
    )
}
