import { createFileRoute } from "@tanstack/react-router"

import { CallToAction } from "@/features/landing/components/cta"
import { Footer } from "@/features/landing/components/footer"
import { Hero } from "@/features/landing/components/hero"
import { Container } from "@/libs/ui/components/container"

export const Route = createFileRoute("/(auth)/")({
    component: LandingPage
})

function LandingPage() {
    return (
        <Container className="max-w-md">
            <Hero />
            <CallToAction />
            <Footer />
        </Container>
    )
}
