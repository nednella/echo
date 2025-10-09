import { Link } from "@tanstack/react-router"

import { Button } from "@/libs/ui/button"
import { MotionContainer } from "@/libs/ui/container"
import { LabelledSeparator } from "@/libs/ui/separator"

export function CallToAction() {
    return (
        <MotionContainer
            className="mt-12"
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            transition={{ duration: 0.6, delay: 0.2 }}
        >
            <Button
                asChild
                fill
                rounded
                size="lg"
                variant="custom"
                className="bg-echo-600 text-neutral-50 hover:opacity-80"
            >
                <Link to="/register">Create an account</Link>
            </Button>
            <LabelledSeparator
                className="my-4 before:bg-neutral-300/40 after:bg-neutral-300/40"
                childrenClassName="text-neutral-100/60"
            >
                or
            </LabelledSeparator>
            <Button
                asChild
                fill
                rounded
                size="lg"
                variant="custom"
                className="bg-neutral-50 text-neutral-900 hover:opacity-80"
            >
                <Link to="/login">Login</Link>
            </Button>
        </MotionContainer>
    )
}
