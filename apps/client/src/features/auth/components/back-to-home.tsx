import { Link } from "@tanstack/react-router"
import { ArrowLeft } from "lucide-react"

import { Button } from "@/libs/ui/components/button"
import { MotionContainer } from "@/libs/ui/components/container"

export function BackToHome() {
    return (
        <MotionContainer
            initial={{ opacity: 0, x: -20 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ duration: 0.8 }}
            className="mb-4"
        >
            <MotionContainer
                whileHover={{ x: -10 }}
                transition={{ type: "spring", stiffness: 400, damping: 20 }}
            >
                <Button
                    asChild
                    variant="custom"
                    className="text-neutral-100/80 hover:bg-neutral-800/30"
                >
                    <Link to="/">
                        <ArrowLeft size={16} />
                        Back to home
                    </Link>
                </Button>
            </MotionContainer>
        </MotionContainer>
    )
}
