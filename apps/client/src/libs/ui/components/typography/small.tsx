import { cn } from "@/libs/ui/utils"

type SmallProps = Readonly<{
    className?: string
    children: React.ReactNode
}>

export function Small({ className, children }: SmallProps) {
    return <small className={cn("text-sm leading-none font-medium", className)}>{children}</small>
}
