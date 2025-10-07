import { createFileRoute } from "@tanstack/react-router"

import { Container } from "@/common/components/container"
import { Page } from "@/common/components/page"
import { CallToAction } from "@/features/landing/components/cta"
import { Footer } from "@/features/landing/components/footer"
import { Hero } from "@/features/landing/components/hero"

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
