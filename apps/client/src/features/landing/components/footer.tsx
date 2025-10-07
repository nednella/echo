import { MotionContainer } from "@/common/components/container"

export function Footer() {
    return (
        <MotionContainer
            className="mt-12 text-center"
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            transition={{ duration: 0.6, delay: 0.8 }}
        >
            <p className="text-xs leading-relaxed text-white/60">By signing up, you agree to the use of Cookies.</p>
        </MotionContainer>
    )
}
