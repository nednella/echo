import { ArrowLeft } from "lucide-react"

import { MotionContainer } from "@/common/components/container"
import { ButtonLink } from "@/libs/ui/button-link"

export function BackToHome() {
    return (
        <MotionContainer
            initial={{ opacity: 0, x: -40 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ duration: 0.8 }}
            className="mb-4"
        >
            <MotionContainer
                whileHover={{ x: -10 }}
                transition={{ type: "spring", stiffness: 400, damping: 20 }}
            >
                <ButtonLink
                    to="/"
                    className="flex w-fit items-center gap-2 rounded-lg px-4 text-sm text-gray-300 hover:bg-gray-800/50
                        hover:text-white"
                >
                    <ArrowLeft size={16} />
                    Back to Home
                </ButtonLink>
            </MotionContainer>
        </MotionContainer>
    )
}
