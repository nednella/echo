import React from "react"

import { cn } from "@/libs/ui/utils"

type Props = React.ComponentPropsWithoutRef<"div"> & { pad?: boolean; center?: boolean; landingGradient?: boolean }

export function Page({ className, pad = false, center = false, landingGradient = false, ...props }: Readonly<Props>) {
    return (
        <div
            className={cn(
                "h-screen w-screen",
                className,
                pad && "p-2",
                center && "flex items-center justify-center",
                landingGradient && "from-gradient-teal to-gradient-navy bg-gradient-to-br"
            )}
            {...props}
        />
    )
}
