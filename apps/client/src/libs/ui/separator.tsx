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

type LabelledSeparatorProps = React.ComponentProps<typeof SeparatorPrimitive.Root> & { label: string }

function LabelledSeparator({ className, label, decorative = true, ...props }: Readonly<LabelledSeparatorProps>) {
    return (
        <div className={cn("flex items-center gap-2", className)}>
            <SeparatorPrimitive.Root
                data-slot="separator"
                decorative={decorative}
                className={"bg-border data-[orientation=horizontal]:h-px data-[orientation=horizontal]:w-full"}
                {...props}
            />
            <span className="text-sm select-none">{label}</span>
            <SeparatorPrimitive.Root
                data-slot="separator"
                decorative={decorative}
                className={"bg-border data-[orientation=horizontal]:h-px data-[orientation=horizontal]:w-full"}
                {...props}
            />
        </div>
    )
}

export { Separator, LabelledSeparator }
