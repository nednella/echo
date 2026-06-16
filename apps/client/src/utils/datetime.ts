const MINUTE = 60
const HOUR = 60 * MINUTE
const DAY = 24 * HOUR
const WEEK = 7 * DAY

/**
 * Compact relative time formatter, falling back to absolute date beyond 1 week.
 * @param iso Date in ISO string format
 * @returns Relative timestamp
 */
export function relativeTime(iso: string): string {
    const seconds = Math.max(0, Math.floor((Date.now() - new Date(iso).getTime()) / 1000))

    if (seconds < MINUTE) return `${seconds}s`
    if (seconds < HOUR) return `${Math.floor(seconds / MINUTE)}m`
    if (seconds < DAY) return `${Math.floor(seconds / HOUR)}h`
    if (seconds < WEEK) return `${Math.floor(seconds / DAY)}d`

    return new Date(iso).toLocaleDateString(undefined, { month: "short", day: "numeric" })
}
