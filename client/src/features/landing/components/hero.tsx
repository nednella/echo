import { MotionContainer } from "@/libs/ui/container"
import { EchoLogo } from "@/libs/ui/echo-logo"

export function Hero() {
    return (
        <MotionContainer
            className="text-center"
            initial={{ opacity: 0, y: 0 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 1.2, delay: 0.2 }}
        >
            <MotionContainer
                initial={{ scale: 0.8 }}
                animate={{ scale: 1 }}
                transition={{ duration: 1.4 }}
            >
                <EchoLogo
                    size={96}
                    variant="light-gradient"
                    className="mx-auto"
                />
            </MotionContainer>
            <h1 className="mt-6 text-3xl font-bold text-white">Join the conversation</h1>
            <p className="mt-12 text-xl text-white/80">
                the social platform where <br /> authentic conversations flourish
            </p>
        </MotionContainer>
    )
}
