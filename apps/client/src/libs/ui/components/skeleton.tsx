import { cn } from "@/libs/ui/utils.ts"

function Skeleton({ className, ...props }: Readonly<React.ComponentProps<"div">>) {
    return (
        <div
            data-slot="skeleton"
            className={cn("bg-accent animate-pulse rounded-md", className)}
            {...props}
        />
    )
}

export { Skeleton }
