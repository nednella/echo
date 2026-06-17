import React from "react"

import { motion } from "motion/react"
import { twMerge } from "tailwind-merge"

type ContainerProps = Readonly<
    React.ComponentPropsWithoutRef<"div"> & {
        px?: "0" | "2" | "4" | "6" | "8"
    }
>

const pxMap = {
    0: "",
    2: "p-2",
    4: "p-4",
    6: "p-6",
    8: "p-8"
} as const

export function Container({ className, px = "0", ...props }: ContainerProps) {
    return (
        <div
            className={twMerge("mx-auto w-full", pxMap[px], className)}
            {...props}
        />
    )
}

export const MotionContainer = motion.create(Container)
