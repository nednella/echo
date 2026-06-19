import { toast } from "sonner"

export const copyLink = async (path: string) => {
    try {
        await navigator.clipboard.writeText(`${globalThis.location.origin}${path}`)
        toast.success("Copied to clipboard")
    } catch {
        toast.error("Could not copy link")
    }
}
