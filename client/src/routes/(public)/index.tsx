import Buttons from "../../features/landing/components/buttons"
import Footer from "../../features/landing/components/footer"
import Hero from "../../features/landing/components/hero"
import Layout from "../../features/landing/layout/layout"
import { createFileRoute } from "@tanstack/react-router"

export const Route = createFileRoute("/(public)/")({
    component: LandingPage
})

function LandingPage() {
    return (
        <Layout>
            <Hero />
            <Buttons />
            <Footer />
        </Layout>
    )
}
