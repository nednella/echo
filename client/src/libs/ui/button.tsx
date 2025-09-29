import React from "react"

import { twMerge } from "tailwind-merge"

type Props = React.ComponentPropsWithRef<"button">

// TODO: theme

export function Button({ className, type = "button", ...props }: Readonly<Props>) {
    return (
        <button
            className={twMerge(
                `w-full cursor-pointer truncate rounded-full border border-transparent px-8 py-2 font-medium transition
                select-none hover:opacity-80 hover:active:border-current disabled:cursor-not-allowed
                disabled:opacity-75`,
                className
            )}
            type={type}
            {...props}
        />
    )
}
