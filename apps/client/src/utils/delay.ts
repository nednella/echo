export const delayWithPromise = (delayMs: number) => {
    return new Promise<void>((resolve) => {
        setTimeout(resolve, delayMs)
    })
}
