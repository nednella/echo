import { cn } from "@/libs/utils"

interface Props {
    className?: string
    children: React.ReactNode
}
export default function Small({ className, children }: Readonly<Props>) {
    return <small className={cn("text-sm leading-none font-medium", className)}>{children}</small>
}
