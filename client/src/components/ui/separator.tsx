import React from "react"

import { twMerge } from "tailwind-merge"

interface HrProps extends React.ComponentPropsWithoutRef<"hr"> {
    colour?: string
    thickness?: "thin" | "thick"
}

// TODO: theme

function Hr({ className, colour, thickness = "thin", ...props }: Readonly<HrProps>) {
    return (
        <hr
            className={twMerge(
                "my-8 block w-full border-0",
                colour,
                thickness === "thin" ? "h-px" : "h-[2px]",
                className
            )}
            {...props}
        />
    )
}

interface SeparatorProps {
    className?: string
    colour?: HrProps["colour"]
    label?: React.ReactNode
    thickness?: HrProps["thickness"]
}

export function Separator({ className, colour, label, thickness = "thin" }: Readonly<SeparatorProps>) {
    if (!label) {
        return (
            <Hr
                className={className}
                colour={colour}
                thickness={thickness}
            />
        )
    }

    return (
        <div className={twMerge("my-6 flex items-center gap-2", className)}>
            <Hr
                className="my-0 flex-1"
                colour={colour}
                thickness={thickness}
            />
            <span className="px-2 text-sm text-gray-500 select-none">{label}</span>
            <Hr
                className="my-0 flex-1"
                colour={colour}
                thickness={thickness}
            />
        </div>
    )
}
