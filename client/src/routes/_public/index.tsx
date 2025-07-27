import { useState } from "react"

import { SignedIn, SignedOut, SignInButton, UserButton } from "@clerk/clerk-react"
import { createFileRoute, Link } from "@tanstack/react-router"

export const Route = createFileRoute("/_public/")({
    component: Index
})

function Index() {
    const [count, setCount] = useState(0)

    return (
        <div className="flex h-full flex-col items-center justify-center gap-6">
            <div className="flex gap-6">
                <SignedIn>
                    <UserButton />
                </SignedIn>
                <SignedOut>
                    <SignInButton />
                </SignedOut>
                <h1 className="text-3xl font-medium">Echo</h1>
            </div>
            <button
                className="pl cursor-pointer rounded-lg border border-white px-4 py-2 transition-colors
                    hover:border-[#646cff]"
                onClick={() => setCount((count) => count + 1)}
            >
                count is {count}
            </button>
            <Link to="/home">Home</Link>
        </div>
    )
}
