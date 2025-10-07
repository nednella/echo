import { cn } from "@/libs/utils"

interface Props {
    className?: string
    children: React.ReactNode
}
export default function Large({ className, children }: Readonly<Props>) {
    return <div className={cn("text-lg font-semibold", className)}>{children}</div>
}
