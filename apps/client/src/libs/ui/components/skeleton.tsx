import { cn } from "@/libs/ui/utils.ts"

type SkeletonProps = Readonly<React.ComponentProps<"div">>

function Skeleton({ className, ...props }: SkeletonProps) {
    return (
        <div
            data-slot="skeleton"
            className={cn("bg-accent animate-pulse rounded-md", className)}
            {...props}
        />
    )
}

export { Skeleton }
