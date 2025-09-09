import { MotionContainer } from "../../../components/container"
import { EchoLogo } from "../../../components/echo-logo"

export function Hero() {
    return (
        <MotionContainer
            className="text-center"
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            transition={{ duration: 1.2, delay: 0.2 }}
        >
            <EchoLogo
                size={96}
                variant="gradient"
                className="mx-auto"
            />
            <h1 className="mt-12 text-3xl font-bold text-gray-900">
                Your voice, <span className="text-echo-teal">heard</span>
            </h1>
            <p className="mt-6 text-xl text-gray-600">
                the social platform where <br /> authentic conversations flourish
            </p>
        </MotionContainer>
    )
}
