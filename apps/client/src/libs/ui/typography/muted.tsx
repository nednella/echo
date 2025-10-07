import { cn } from "@/libs/utils"

interface Props {
    className?: string
    children: React.ReactNode
}
export default function Muted({ className, children }: Readonly<Props>) {
    return <p className={cn("text-muted-foreground text-sm", className)}>{children}</p>
}
