import { toast } from "sonner"

export const copyCurrentUrl = async () => {
    try {
        await navigator.clipboard.writeText(globalThis.location.href)
        toast.success("Copied to clipboard")
    } catch {
        toast.error("Could not copy link")
    }
}
