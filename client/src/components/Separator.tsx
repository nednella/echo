import { twMerge } from "tailwind-merge"

interface Props {
    className?: string
}

export default function Separator({ className }: Readonly<Props>) {
    return <hr className={twMerge("my-4 w-full border-neutral-400", className)} />
}
