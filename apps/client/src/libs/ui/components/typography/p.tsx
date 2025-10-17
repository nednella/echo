import { cn } from "@/libs/ui/utils"

interface Props {
    className?: string
    children: React.ReactNode
}

export function P({ className, children }: Readonly<Props>) {
    return <p className={cn("leading-7 [&:not(:first-child)]:mt-6", className)}>{children}</p>
}
