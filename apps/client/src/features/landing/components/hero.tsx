import { EchoLogo } from "@/common/components/logos/echo-logo"
import { MotionContainer } from "@/libs/ui/components/container"
import { H1 } from "@/libs/ui/components/typography/h1"
import { Lead } from "@/libs/ui/components/typography/lead"

export function Hero() {
    return (
        <MotionContainer
            className="text-center"
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            transition={{ duration: 0.6, delay: 0.1 }}
        >
            <EchoLogo
                size={80}
                variant="light-gradient"
                className="mx-auto"
            />
            <H1 className="mt-4 text-neutral-100">Join the conversation</H1>
            <Lead className="mt-4 text-neutral-100/60">
                the social platform where <br /> authentic conversations flourish
            </Lead>
        </MotionContainer>
    )
}
