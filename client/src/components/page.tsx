import type React from "react"

import { twMerge } from "tailwind-merge"

interface Props {
    children: React.ReactNode
    className?: string
}

export default function Page({ children, className }: Readonly<Props>) {
    // TODO: theme
    return <div className={twMerge("min-h-screen bg-gray-50 p-2", className)}>{children}</div>
}
