import { useEffect, useRef } from "react"

/**
 * Custom hook to ensure a useEffect hook is invoked once only, tracked with a ref.
 *
 * A small timeout is added to prevent double mutations in strict mode during development.
 *
 * See: https://github.com/TanStack/query/issues/5341
 */
export const useEffectOnce = (effect: React.EffectCallback) => {
    const didRun = useRef(false)

    useEffect(() => {
        const id = setTimeout(() => {
            if (!didRun.current) {
                didRun.current = true

                const destructor = effect()
                if (destructor) return destructor
            }
        }, 50)

        return () => clearTimeout(id)
    }, [effect])
}
