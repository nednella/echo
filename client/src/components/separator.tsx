import React from "react"

import { twMerge } from "tailwind-merge"

type HrProps = React.ComponentPropsWithoutRef<"hr"> & {
    thickness?: "thin" | "thick"
    colour?: string
}

function Hr({ thickness = "thin", colour, className, ...props }: Readonly<HrProps>) {
    return (
        <hr
            {...props}
            className={twMerge(
                "my-8 block w-full border-0",
                thickness === "thin" ? "h-px" : "h-[2px]",
                colour,
                className
            )}
        />
    )
}

interface SeparatorProps {
    label?: React.ReactNode
    thickness?: HrProps["thickness"]
    colour?: HrProps["colour"]
    className?: string
}

export function Separator({ label, thickness = "thin", colour, className }: Readonly<SeparatorProps>) {
    if (!label) {
        return (
            <Hr
                thickness={thickness}
                colour={colour}
                className={className}
            />
        )
    }

    return (
        <div className={twMerge("my-6 flex items-center gap-2", className)}>
            <Hr
                thickness={thickness}
                colour={colour}
                className="my-0 flex-1"
            />
            <span className="px-2 text-sm text-gray-500 select-none">{label}</span>
            <Hr
                thickness={thickness}
                colour={colour}
                className="my-0 flex-1"
            />
        </div>
    )
}
