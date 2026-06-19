import React from "react"

import { cn } from "@/libs/ui/utils"

type AppRouteProps = Readonly<React.ComponentPropsWithoutRef<"div">> & { center?: boolean }

/**
 * in-app route shell
 */
export function AppRoute({ center, className, ...props }: AppRouteProps) {
    return (
        <div
            className={cn("flex w-full flex-1 flex-col", center && "flex items-center justify-center", className)}
            {...props}
        />
    )
}
