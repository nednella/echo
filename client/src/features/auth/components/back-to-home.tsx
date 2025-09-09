import ButtonLink from "../../../components/button-link"
import { ArrowLeft } from "lucide-react"
import { motion } from "motion/react"

export default function BackToHome() {
    return (
        <motion.div
            whileHover={{ x: -5 }}
            transition={{ type: "spring", stiffness: 400, damping: 20 }}
            className="mb-4"
        >
            <ButtonLink
                to="/"
                className="flex w-fit items-center gap-2 rounded-lg px-4 text-sm text-gray-300 hover:bg-gray-800/50
                    hover:text-white"
            >
                <ArrowLeft size={16} />
                Back to Home
            </ButtonLink>
        </motion.div>
    )
}
