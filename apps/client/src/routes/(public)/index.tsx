import { createFileRoute } from "@tanstack/react-router"

import { CallToAction } from "@/features/landing/components/cta"
import { Footer } from "@/features/landing/components/footer"
import { Hero } from "@/features/landing/components/hero"
import { Container } from "@/libs/ui/container"
import { Page } from "@/libs/ui/page"

export const Route = createFileRoute("/(public)/")({
    component: LandingPage
})

function LandingPage() {
    return (
        <Page
            center
            landingGradient
        >
            <Container className="max-w-md">
                <Hero />
                <CallToAction />
                <Footer />
            </Container>
        </Page>
    )
}
