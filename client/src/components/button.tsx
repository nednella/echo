import React from "react"

import { twMerge } from "tailwind-merge"

type Props = React.ComponentPropsWithRef<"button">

export function Button({ children, className, type = "button", ...props }: Readonly<Props>) {
    return (
        <button
            className={twMerge(
                // TODO: theme
                `w-full cursor-pointer truncate rounded-full px-8 py-2 font-medium transition select-none
                hover:opacity-90 disabled:cursor-not-allowed disabled:opacity-75`,
                className
            )}
            type={type}
            {...props}
        >
            {children}
        </button>
    )
}
