import { createFileRoute } from "@tanstack/react-router"

import { CallToAction } from "@/features/landing/ui/cta"
import { Footer } from "@/features/landing/ui/footer"
import { Hero } from "@/features/landing/ui/hero"
import { Container } from "@/libs/ui/container"
import { Page } from "@/libs/ui/page"

export const Route = createFileRoute("/(public)/")({
    component: LandingPage
})

function LandingPage() {
    return (
        <Page className="to-echo-teal from-echo-navy flex items-center justify-center bg-gradient-to-br">
            <Container className="max-w-md">
                <Hero />
                <CallToAction />
                <Footer />
            </Container>
        </Page>
    )
}
