import { createFileRoute, Link } from "@tanstack/react-router"

export const Route = createFileRoute("/(public)/")({
    component: RouteComponent
})

function RouteComponent() {
    return (
        <div className="grid gap-2 p-2">
            <h1 className="text-xl">Welcome!</h1>
            <p>You are currently on the landing page.</p>
            <ol className="list-inside list-disc px-2">
                <li>
                    <Link
                        to="/auth/login"
                        className="text-blue-500 hover:opacity-75"
                    >
                        Go to the public login page.
                    </Link>
                </li>
                <li>
                    <Link
                        to="/auth/register"
                        className="text-blue-500 hover:opacity-75"
                    >
                        Go to the public register page.
                    </Link>
                </li>
                <li>
                    <Link
                        to="/home"
                        className="text-blue-500 hover:opacity-75"
                    >
                        Go to the auth-only home page.
                    </Link>
                </li>
                <li>
                    <Link
                        to="/onboarding"
                        className="text-blue-500 hover:opacity-75"
                    >
                        Go to the auth-only onboarding page.
                    </Link>
                </li>
            </ol>
        </div>
    )
}
