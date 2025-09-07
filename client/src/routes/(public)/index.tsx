import ButtonLink from "../../components/ButtonLink"
import EchoLogo from "../../components/EchoLogo"
import Page from "../../components/Page"
import Separator from "../../components/Separator"
import { createFileRoute } from "@tanstack/react-router"
import { motion } from "motion/react"

export const Route = createFileRoute("/(public)/")({
    component: RouteComponent
})

function RouteComponent() {
    return (
        <Page className="flex items-center justify-center">
            <section className="w-full max-w-md">
                {/* Logo */}
                <motion.div
                    className="text-center"
                    initial={{ opacity: 0 }}
                    animate={{ opacity: 1 }}
                    transition={{ duration: 1.2, delay: 0.2 }}
                >
                    <EchoLogo
                        size={96}
                        variant="gradient"
                        className="mx-auto"
                    />
                    <h1 className="mt-12 text-3xl font-bold text-gray-900">
                        Your Voice, <span className="text-echo-teal">Heard</span>
                    </h1>
                    <p className="mt-6 text-xl text-gray-600">
                        the social platform where <br /> authentic conversations flourish
                    </p>
                </motion.div>

                {/* Buttons */}
                <motion.div
                    className="mt-12"
                    initial={{ y: 20, opacity: 0 }}
                    animate={{ y: 0, opacity: 1 }}
                    transition={{ duration: 0.6, delay: 0.4 }}
                >
                    <ButtonLink
                        to={"/register"}
                        className="bg-echo-teal text-white shadow-sm"
                    >
                        Create an account
                    </ButtonLink>
                    <Separator
                        colour="bg-gray-200"
                        label={"or"}
                    />
                    <ButtonLink
                        to={"/login"}
                        className="bg-white shadow-sm"
                    >
                        Login
                    </ButtonLink>
                </motion.div>

                {/* Footer */}
                <motion.div
                    className="mt-12 text-center"
                    initial={{ opacity: 0 }}
                    animate={{ opacity: 1 }}
                    transition={{ duration: 0.6, delay: 0.8 }}
                >
                    <p className="text-xs leading-relaxed text-gray-500">
                        By signing up, you agree to the use of Cookies.
                    </p>
                </motion.div>
            </section>
        </Page>
    )
}
