import { Link } from "@tanstack/react-router"

import { Button } from "@/libs/ui/components/button"
import { Container } from "@/libs/ui/components/container"
import { H1 } from "@/libs/ui/components/typography/h1"
import { Lead } from "@/libs/ui/components/typography/lead"

import { EchoLogo } from "../logos/echo-logo"
import { AppRoute } from "./app-route"

type AppRouteNotFoundProps = Readonly<{
    path?: string
}>

export function AppRouteNotFound({ path }: AppRouteNotFoundProps) {
    const heading = `${path ?? "page"} not found`
    const subheading = `we can't seem to find the ${path ?? "page"} you are looking for`
    return (
        <AppRoute center>
            <Container className="flex flex-col items-center gap-8">
                <EchoLogo
                    size={64}
                    variant="light-gradient"
                />
                <div>
                    <H1 className="text-neutral-100">{heading}</H1>
                    <Lead className="mt-2 text-neutral-100/60">{subheading}</Lead>
                </div>
                <Button
                    asChild
                    rounded
                    size="lg"
                    variant="custom"
                    className="bg-neutral-50 text-neutral-900 hover:opacity-80"
                >
                    <Link to="/">home</Link>
                </Button>
            </Container>
        </AppRoute>
    )
}
