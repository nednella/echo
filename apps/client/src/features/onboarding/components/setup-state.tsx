import { useEffect, useState } from "react"

import { useUser } from "@clerk/clerk-react"
import { Check } from "lucide-react"
import { motion } from "motion/react"

import { EchoLogo } from "@/components/logos/echo-logo"
import { cn } from "@/libs/ui/utils"

const STEPS = ["Creating your profile", "Curating your feed", "Finishing touches"]
const STEP_INTERVAL_MS = 650
const fade = {
    initial: { opacity: 0, y: 6 },
    animate: { opacity: 1, y: 0 },
    exit: { opacity: 0, y: -6 },
    transition: { duration: 0.25 }
}

type StepState = "done" | "active" | "pending"

export function SetupState() {
    const { user } = useUser()
    const [active, setActive] = useState(0)

    useEffect(() => {
        const id = setInterval(() => setActive((index) => Math.min(index + 1, STEPS.length - 1)), STEP_INTERVAL_MS)
        return () => clearInterval(id)
    }, [])

    const progress = 12 + (active / (STEPS.length - 1)) * 76

    return (
        <motion.div {...fade}>
            <div className="relative mx-auto mb-6 w-fit">
                {/* soft brand glow behind the logo */}
                <motion.span
                    aria-hidden
                    className="bg-echo-500/30 absolute inset-0 -z-10 rounded-full blur-xl"
                    animate={{ scale: [0.85, 1.15, 0.85], opacity: [0.35, 0.65, 0.35] }}
                    transition={{ duration: 2.4, repeat: Number.POSITIVE_INFINITY, ease: "easeInOut" }}
                />
                <EchoLogo
                    variant="light-gradient"
                    size={48}
                    className="mx-auto"
                />
            </div>

            <h1 className="text-xl font-semibold tracking-tight">Setting up your space</h1>
            <p className="text-muted-foreground mt-2 text-sm">{`Hang tight, ${user?.username} — we're getting echo ready for you.`}</p>

            <ul className="mt-7 flex flex-col gap-1 text-left">
                {STEPS.map((label, index) => (
                    <Step
                        key={label}
                        label={label}
                        state={index < active ? "done" : index === active ? "active" : "pending"}
                    />
                ))}
            </ul>

            <div className="bg-muted mt-6 h-1.5 overflow-hidden rounded-full">
                <motion.div
                    className="from-echo-600 to-echo-400 h-full rounded-full bg-linear-to-r"
                    initial={{ width: "8%" }}
                    animate={{ width: `${progress}%` }}
                    transition={{ duration: 0.5, ease: "easeOut" }}
                />
            </div>
            <p className="text-muted-foreground mt-4 text-xs">This only takes a moment…</p>
        </motion.div>
    )
}

type StepProps = Readonly<{
    label: string
    state: StepState
}>

function Step({ label, state }: StepProps) {
    return (
        <li
            className={cn(
                "flex items-center gap-3 rounded-lg px-3 py-2.5 text-sm transition-colors",
                state === "pending" ? "text-muted-foreground" : "text-foreground"
            )}
        >
            <span className="grid size-5 shrink-0 place-items-center">
                {state === "done" && (
                    <span className="bg-echo-500 grid size-5 place-items-center rounded-full text-white">
                        <Check
                            size={12}
                            strokeWidth={3}
                        />
                    </span>
                )}
                {state === "active" && (
                    <span className="border-border border-t-echo-400 size-5 animate-spin rounded-full border-2" />
                )}
                {state === "pending" && <span className="border-border size-5 rounded-full border-2" />}
            </span>
            {label}
        </li>
    )
}
