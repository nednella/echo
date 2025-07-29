import React, { forwardRef } from "react"

import { twMerge } from "tailwind-merge"

type Props = React.ButtonHTMLAttributes<HTMLButtonElement>

const Button = forwardRef<HTMLButtonElement, Props>(
    ({ children, className, disabled, type = "button", ...props }, ref) => (
        <button
            type={type}
            disabled={disabled}
            className={twMerge(
                // TODO: bg-colour/text -> theme
                `w-full cursor-pointer truncate rounded-lg bg-cyan-500 px-8 py-2 font-medium transition select-none
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

export default Button
