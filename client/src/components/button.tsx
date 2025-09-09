import React, { forwardRef } from "react"

import { twMerge } from "tailwind-merge"

type Props = React.ButtonHTMLAttributes<HTMLButtonElement>

export const Button = forwardRef<HTMLButtonElement, Props>(
    ({ children, className, disabled, type = "button", ...props }, ref) => (
        <button
            type={type}
            disabled={disabled}
            className={twMerge(
                // TODO: theme
                `w-full cursor-pointer truncate rounded-full px-8 py-2 font-medium transition select-none
                hover:opacity-90 disabled:cursor-not-allowed disabled:opacity-75`,
                className
            )}
            ref={ref}
            {...props}
        >
            {children}
        </button>
    )
)
