import { cn } from "@/libs/ui/utils"

type H3Props = Readonly<{
    className?: string
    children: React.ReactNode
}>

export function H3({ className, children }: H3Props) {
    return <h3 className={cn("scroll-m-20 text-2xl font-semibold tracking-tight", className)}>{children}</h3>
}
