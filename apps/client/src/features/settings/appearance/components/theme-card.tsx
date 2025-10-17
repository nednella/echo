import { CircleCheck } from "lucide-react"

import { Small } from "@/libs/ui/components/typography/small"
import { cn } from "@/libs/ui/utils"

import type { ThemeOption } from "./theme-card.config"

interface Props {
    active: boolean
    label: ThemeOption["label"]
    src: ThemeOption["src"]
    onClick: () => void
}

// TODO: add info colour to tailwind --> blue-500

export function ThemeCard({ active, label, src, onClick }: Readonly<Props>) {
    return (
        <div className="flex max-w-40 flex-col gap-2">
            <button
                className={cn("ring-border relative rounded-md ring-2", active && "ring-[#2b7fff]")}
                onClick={onClick}
            >
                <div className="h-full w-full overflow-hidden rounded-md">
                    <img
                        src={src}
                        alt={label}
                        draggable={false}
                        className="object-cover"
                    />
                </div>
                {active && (
                    <CircleCheck
                        className="text-primary absolute top-0 right-0 translate-x-1/2 -translate-y-1/2 fill-[#2b7fff]
                            stroke-white"
                    />
                )}
            </button>
            <Small>{label}</Small>
        </div>
    )
}
