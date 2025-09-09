import React from "react"

import { twMerge } from "tailwind-merge"

interface Props {
    children: React.ReactNode
    className?: string
}

export function Card({ children, className }: Readonly<Props>) {
    // TODO: bg-colour/text -> theme
    return <div className={twMerge("h-fit w-full rounded-lg bg-white p-4 shadow-md", className)}>{children}</div>
}
