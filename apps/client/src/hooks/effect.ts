import { useEffect, useRef } from "react"

export const useEffectOnce = (effect: React.EffectCallback) => {
    const didRun = useRef(false)

    useEffect(() => {
        if (!didRun.current) {
            didRun.current = true

            const destructor = effect()
            if (destructor) return destructor
        }

        return () => {}
    }, [effect])
}
