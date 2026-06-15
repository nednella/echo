import React from "react"

import { cn } from "@/libs/ui/utils"

/**
 * feed content column
 */
export function FeedContainer({ className, ...props }: Readonly<React.ComponentPropsWithoutRef<"div">>) {
    return (
        <div
            className={cn("mx-auto flex w-full max-w-[600px] flex-1 flex-col", className)}
            {...props}
        />
    )
}
