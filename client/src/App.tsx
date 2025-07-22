import { useState } from "react"

export default function App() {
    const [count, setCount] = useState(0)

    return (
        <div className="flex h-full flex-col items-center justify-center gap-6">
            <h1>Echo</h1>
            <button
                className="pl cursor-pointer rounded-lg border border-white px-4 py-2 transition-colors
                    hover:border-[#646cff]"
                onClick={() => setCount((count) => count + 1)}
            >
                count is {count}
            </button>
        </div>
    )
}
