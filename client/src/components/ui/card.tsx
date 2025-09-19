import React from "react"

import { twMerge } from "tailwind-merge"

type Props = React.ComponentPropsWithoutRef<"div">

// TODO: theme

export function Card({ className, ...props }: Readonly<Props>) {
    return (
        <div
            className={twMerge("h-fit w-full rounded-lg bg-white p-4 shadow-md", className)}
            {...props}
        />
    )
}
