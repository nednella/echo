import { cn } from "@/libs/ui/utils"

type PProps = Readonly<{
    className?: string
    children: React.ReactNode
}>

export function P({ className, children }: PProps) {
    return <p className={cn("leading-7 not-first:mt-6", className)}>{children}</p>
}
