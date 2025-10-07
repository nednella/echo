import { Link } from "@tanstack/react-router"
import { ArrowLeft } from "lucide-react"

import { Button } from "@/libs/ui/button"
import { MotionContainer } from "@/libs/ui/container"

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
                <Button
                    asChild
                    size="sm"
                    variant="ghost"
                    className="text-neutral-100/80"
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
