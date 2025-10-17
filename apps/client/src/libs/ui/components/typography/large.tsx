import { cn } from "@/libs/ui/utils"

interface Props {
    className?: string
    children: React.ReactNode
}

export function Large({ className, children }: Readonly<Props>) {
    return <div className={cn("text-lg font-semibold", className)}>{children}</div>
}
