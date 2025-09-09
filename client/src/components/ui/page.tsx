import React from "react"

import { twMerge } from "tailwind-merge"

type Props = React.ComponentPropsWithoutRef<"div">

export function Page({ className, ...props }: Readonly<Props>) {
    // TODO: theme
    return (
        <div
            className={twMerge("h-screen w-screen bg-gray-50 p-2", className)}
            {...props}
        />
    )
}
