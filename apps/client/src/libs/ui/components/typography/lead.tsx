import { cn } from "@/libs/ui/utils"

type LeadProps = Readonly<{
    className?: string
    children: React.ReactNode
}>

export function Lead({ className, children }: LeadProps) {
    return <p className={cn("text-muted-foreground text-xl", className)}>{children}</p>
}
