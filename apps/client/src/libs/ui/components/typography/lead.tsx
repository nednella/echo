import { cn } from "@/libs/utils"

interface Props {
    className?: string
    children: React.ReactNode
}
export default function Lead({ className, children }: Readonly<Props>) {
    return <p className={cn("text-muted-foreground text-xl", className)}>{children}</p>
}
