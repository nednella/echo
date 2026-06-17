import React from "react"

import { cn } from "@/libs/ui/utils"

type PageProps = Readonly<
    React.ComponentPropsWithoutRef<"div"> & { pad?: boolean; center?: boolean; landingGradient?: boolean }
>

/**
 * static page shell
 */
export function Page({ className, pad = false, center = false, landingGradient = false, ...props }: PageProps) {
    return (
        <div
            className={cn(
                "h-screen w-screen",
                className,
                pad && "p-2",
                center && "flex items-center justify-center",
                landingGradient && "from-gradient-teal to-gradient-navy bg-linear-to-br"
            )}
            {...props}
        />
    )
}
