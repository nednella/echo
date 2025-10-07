import { Link } from "@tanstack/react-router"

import { Button } from "@/libs/ui/button"
import { MotionContainer } from "@/libs/ui/container"
import { LabelledSeparator } from "@/libs/ui/separator"

export function CallToAction() {
    return (
        <MotionContainer
            className="mt-12"
            initial={{ y: 20, opacity: 0 }}
            animate={{ y: 0, opacity: 1 }}
            transition={{ duration: 0.6, delay: 0.4 }}
        >
            <Button
                asChild
                fill
                rounded
                size="lg"
            >
                <Link to="/register">Create an account</Link>
            </Button>
            <LabelledSeparator
                className="my-4 text-neutral-100 opacity-60"
                label="or"
            />
            <Button
                asChild
                fill
                rounded
                size="lg"
                variant="secondary"
            >
                <Link to="/login">Login</Link>
            </Button>
        </MotionContainer>
    )
}
