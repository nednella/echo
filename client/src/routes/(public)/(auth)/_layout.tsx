import ButtonLink from "../../../components/ButtonLink"
import Page from "../../../components/Page"
import { createFileRoute, Outlet, redirect } from "@tanstack/react-router"
import { ArrowLeft } from "lucide-react"
import { motion } from "motion/react"

// /(public)/(auth) layout component ensuring access only for unauthenticated users
export const Route = createFileRoute("/(public)/(auth)")({
    beforeLoad({ context }) {
        // Redirect to home if user is already authenticated
        if (context.auth.isSignedIn === true) {
            throw redirect({
                to: "/home",
                replace: true
            })
        }
    },
    component: RouteComponent
})

function RouteComponent() {
    return (
        <Page className="to-echo-teal from-echo-navy flex items-center justify-center bg-gradient-to-br">
            <motion.div
                initial={{ y: 20, opacity: 0 }}
                animate={{ y: 0, opacity: 1 }}
                transition={{ duration: 0.6, delay: 0.2 }}
            >
                {/* Back button */}
                <motion.div
                    whileHover={{ x: -5 }}
                    transition={{ type: "spring", stiffness: 400, damping: 20 }}
                    className="mb-4"
                >
                    <ButtonLink
                        to="/"
                        className="flex w-fit items-center gap-2 rounded-lg px-4 text-sm text-gray-300
                            hover:bg-gray-800/50 hover:text-white"
                    >
                        <ArrowLeft size={16} />
                        Back to Home
                    </ButtonLink>
                </motion.div>

                {/* Auth component */}
                <motion.div
                    initial={{ scale: 0.95, opacity: 0 }}
                    animate={{ scale: 1, opacity: 1 }}
                    transition={{ duration: 0.6, delay: 0.2 }}
                >
                    <Outlet />
                </motion.div>
            </motion.div>
        </Page>
    )
}
