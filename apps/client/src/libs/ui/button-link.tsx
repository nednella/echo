import React from "react"

import { type LinkComponent, createLink } from "@tanstack/react-router"
import { twMerge } from "tailwind-merge"

type Props = React.ComponentPropsWithRef<"a">

// TODO: theme

function Link({ className, ...props }: Readonly<Props>) {
    return (
        <a
            className={twMerge(
                `block w-full cursor-pointer truncate rounded-full border border-transparent px-8 py-2 text-center
                font-medium transition select-none hover:opacity-80 hover:active:border-current
                disabled:cursor-not-allowed disabled:opacity-75`,
                className
            )}
            {...props}
        />
    )
}

const CustomLink = createLink(Link)

export const ButtonLink: LinkComponent<typeof Link> = (props) => {
    return <CustomLink {...props} />
}
