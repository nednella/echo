import React from "react"

import { cn } from "@/libs/ui/utils"

type AppPageProps = Readonly<React.ComponentPropsWithoutRef<"div">>

/**
 * in-app route shell
 */
export function AppPage({ className, ...props }: AppPageProps) {
    return (
        <div
            className={cn("flex w-full flex-1 flex-col", className)}
            {...props}
        />
    )
}
