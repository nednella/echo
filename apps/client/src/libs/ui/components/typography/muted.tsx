import { cn } from "@/libs/ui/utils"

interface Props {
    className?: string
    children: React.ReactNode
}

export function Muted({ className, children }: Readonly<Props>) {
    return <p className={cn("text-muted-foreground text-sm", className)}>{children}</p>
}
