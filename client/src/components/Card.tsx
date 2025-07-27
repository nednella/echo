import React from "react"

import { twMerge } from "tailwind-merge"

interface Props {
    children: React.ReactNode
    className?: string
}

export default function Card({ children, className }: Readonly<Props>) {
    // TODO: bg-colour -> theme primary colour
    return <div className={twMerge("h-fit w-full rounded-lg bg-neutral-100 p-2 shadow-md", className)}>{children}</div>
}
