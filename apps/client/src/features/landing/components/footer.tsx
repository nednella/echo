import { MotionContainer } from "@/libs/ui/container"

export function Footer() {
    return (
        <MotionContainer
            className="mt-12 text-center"
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            transition={{ duration: 0.6, delay: 0.3 }}
        >
            <p className="text-xs text-neutral-100/60">
                By signing up, you agree to the use of <strong>Cookies.</strong>
            </p>
        </MotionContainer>
    )
}
