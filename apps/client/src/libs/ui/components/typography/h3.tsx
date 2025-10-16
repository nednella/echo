import { cn } from "@/libs/ui/utils"

interface Props {
    className?: string
    children: React.ReactNode
}
export default function H3({ className, children }: Readonly<Props>) {
    return <h3 className={cn("scroll-m-20 text-2xl font-semibold tracking-tight", className)}>{children}</h3>
}
