import { forwardRef, type AnchorHTMLAttributes } from "react"

import { createLink, type LinkComponent } from "@tanstack/react-router"
import { twMerge } from "tailwind-merge"

interface Props extends AnchorHTMLAttributes<HTMLAnchorElement> {
    className?: string
}

const Link = forwardRef<HTMLAnchorElement, Props>(({ className, ...props }, ref) => {
    return (
        <a
            ref={ref}
            {...props}
            className={twMerge(
                // TODO: theme
                `block w-full cursor-pointer truncate rounded-full px-8 py-2 text-center font-medium transition
                select-none hover:opacity-90 disabled:cursor-not-allowed disabled:opacity-75`,
                className
            )}
        />
    )
})

const CreatedLinkComponent = createLink(Link)

export const ButtonLink: LinkComponent<typeof Link> = (props) => {
    return <CreatedLinkComponent {...props} />
}
