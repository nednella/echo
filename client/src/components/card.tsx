import React from "react"

import { twMerge } from "tailwind-merge"

type Props = React.ComponentPropsWithoutRef<"div">

export function Card({ className, ...props }: Readonly<Props>) {
    // TODO: theme
    return (
        <div
            className={twMerge("h-fit w-full rounded-lg bg-white p-4 shadow-md", className)}
            {...props}
        />
    )
}
