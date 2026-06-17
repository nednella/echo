import React from "react"

import { cn } from "@/libs/ui/utils"

type FeedContainerProps = Readonly<React.ComponentPropsWithoutRef<"div">>

export function FeedContainer({ className, ...props }: FeedContainerProps) {
    return (
        <div
            className={cn("mx-auto flex w-full max-w-150 flex-1 flex-col", className)}
            {...props}
        />
    )
}
