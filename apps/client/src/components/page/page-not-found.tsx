import { Link } from "@tanstack/react-router"

import { Button } from "@/libs/ui/components/button"
import { Container } from "@/libs/ui/components/container"
import { H1 } from "@/libs/ui/components/typography/h1"
import { Lead } from "@/libs/ui/components/typography/lead"

import { EchoLogo } from "../logos/echo-logo"
import { Page } from "./page"

export function PageNotFound() {
    return (
        <Page
            center
            landingGradient
        >
            <Container className="flex flex-col items-center gap-8">
                <EchoLogo
                    size={64}
                    variant="light-gradient"
                />
                <div>
                    <H1 className="text-neutral-100">Page not found</H1>
                    <Lead className="mt-2 text-neutral-100/60">We can't seem to find the page you are looking for</Lead>
                </div>
                <Button
                    asChild
                    rounded
                    size="lg"
                    variant="custom"
                    className="bg-neutral-50 text-neutral-900 hover:opacity-80"
                >
                    <Link to="/">Home</Link>
                </Button>
            </Container>
        </Page>
    )
}
