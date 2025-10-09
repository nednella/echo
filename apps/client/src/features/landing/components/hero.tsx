import { EchoLogo } from "@/common/components/logos/echo-logo"
import { MotionContainer } from "@/libs/ui/container"
import H1 from "@/libs/ui/typography/h1"
import Lead from "@/libs/ui/typography/lead"

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
            <H1 className="mt-4 text-neutral-100">Join the conversation</H1>
            <Lead className="mt-4 text-neutral-100/60">
                the social platform where <br /> authentic conversations flourish
            </Lead>
        </MotionContainer>
    )
}
