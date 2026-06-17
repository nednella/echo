import { cn } from "@/libs/ui/utils"

type MutedProps = Readonly<{
    className?: string
    children: React.ReactNode
}>

export function Muted({ className, children }: MutedProps) {
    return <p className={cn("text-muted-foreground text-sm", className)}>{children}</p>
}
