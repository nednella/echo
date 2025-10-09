import * as React from "react"

import * as SeparatorPrimitive from "@radix-ui/react-separator"

import { cn } from "@/libs/utils"

type SeparatorProps = React.ComponentProps<typeof SeparatorPrimitive.Root>

function Separator({ className, orientation = "horizontal", decorative = true, ...props }: Readonly<SeparatorProps>) {
    return (
        <SeparatorPrimitive.Root
            data-slot="separator"
            orientation={orientation}
            decorative={decorative}
            className={cn(
                `bg-border shrink-0 data-[orientation=horizontal]:h-px data-[orientation=horizontal]:w-full
                data-[orientation=vertical]:h-full data-[orientation=vertical]:w-px`,
                className
            )}
            {...props}
        />
    )
}

type LabelledSeparatorProps = React.ComponentProps<typeof SeparatorPrimitive.Root> & {
    label: string
    labelClassName?: string
}

function LabelledSeparator({
    className,
    label,
    labelClassName,
    decorative = true,
    ...props
}: Readonly<LabelledSeparatorProps>) {
    return (
        <SeparatorPrimitive.Root
            data-slot="separator"
            decorative={decorative}
            className={cn(
                `before:bg-border after:bg-border relative flex items-center gap-2 before:h-px before:flex-1
                before:content-[''] after:h-px after:flex-1 after:content-['']`,
                className
            )}
            {...props}
        >
            <span className={cn("text-border text-sm", labelClassName)}>{label}</span>
        </SeparatorPrimitive.Root>
    )
}

export { Separator, LabelledSeparator }
