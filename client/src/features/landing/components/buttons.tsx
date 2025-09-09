import { ButtonLink } from "../../../components/ui/button-link"
import { MotionContainer } from "../../../components/ui/container"
import { Separator } from "../../../components/ui/separator"

export function Buttons() {
    return (
        <MotionContainer
            className="mt-12"
            initial={{ y: 20, opacity: 0 }}
            animate={{ y: 0, opacity: 1 }}
            transition={{ duration: 0.6, delay: 0.4 }}
        >
            <ButtonLink
                to="/register"
                className="bg-echo-teal text-white shadow-sm"
            >
                Create an account
            </ButtonLink>
            <Separator
                colour="bg-gray-400"
                label={"or"}
            />
            <ButtonLink
                to="/login"
                className="bg-white shadow-sm"
            >
                Login
            </ButtonLink>
        </MotionContainer>
    )
}
