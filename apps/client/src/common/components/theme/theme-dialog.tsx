import { useThemeDialog } from "@/common/stores/theme-dialog.store"
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle } from "@/libs/ui/components/dialog"

export function ThemeDialog() {
    const { isOpen, onClose } = useThemeDialog()

    const onChange = (open: boolean) => {
        if (!open) {
            onClose()
        }
    }

    return (
        <Dialog
            open={isOpen}
            onOpenChange={onChange}
        >
            <DialogContent>
                <DialogHeader>
                    <DialogTitle>Theme Dialog</DialogTitle>
                    <DialogDescription>Description</DialogDescription>
                </DialogHeader>
            </DialogContent>
        </Dialog>
    )
}
