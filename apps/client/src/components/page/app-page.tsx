import React from "react"

import { cn } from "@/libs/ui/utils"

/**
 * in-app route shell
 */
export function AppPage({ className, ...props }: Readonly<React.ComponentPropsWithoutRef<"div">>) {
    return (
        <div
            className={cn("flex min-h-full w-full flex-col", className)}
            {...props}
        />
    )
}
