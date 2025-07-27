import React, { forwardRef } from "react"

import { twMerge } from "tailwind-merge"

type Props = React.ButtonHTMLAttributes<HTMLButtonElement>

const Button = forwardRef<HTMLButtonElement, Props>(
    ({ children, className, disabled, type = "button", ...props }, ref) => (
        <button
            type={type}
            disabled={disabled}
            className={twMerge(
                // TODO: bg-colour -> theme primary colour
                `w-full rounded-full bg-neutral-100 p-3 font-bold transition hover:opacity-75 active:scale-95
                disabled:cursor-not-allowed disabled:opacity-75`,
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
