import { cn } from "@/libs/ui/utils"

type LargeProps = Readonly<{
    className?: string
    children: React.ReactNode
}>

export function Large({ className, children }: LargeProps) {
    return <div className={cn("text-lg font-semibold", className)}>{children}</div>
}
