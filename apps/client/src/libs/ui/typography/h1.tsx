import { cn } from "@/libs/utils"

interface Props {
    className?: string
    children: React.ReactNode
}
export default function H1({ className, children }: Readonly<Props>) {
    return (
        <h1 className={cn("scroll-m-20 text-center text-3xl font-extrabold tracking-tight text-balance", className)}>
            {children}
        </h1>
    )
}
