import { createFileRoute } from "@tanstack/react-router"

import { CallToAction } from "@/features/landing/components/cta"
import { Footer } from "@/features/landing/components/footer"
import { Hero } from "@/features/landing/components/hero"
import { Layout } from "@/features/landing/layout/layout"

export const Route = createFileRoute("/(public)/")({
    component: LandingPage
})

function LandingPage() {
    return (
        <Layout>
            <Hero />
            <CallToAction />
            <Footer />
        </Layout>
    )
}
