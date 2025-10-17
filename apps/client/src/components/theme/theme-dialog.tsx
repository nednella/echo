import { useThemeStore } from "@/libs/theme/theme.store"
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle } from "@/libs/ui/components/dialog"
import { Separator } from "@/libs/ui/components/separator"
import { useThemeDialog } from "@/stores/theme-dialog.store"

import { ThemeCard } from "./theme-card"
import { options } from "./theme-card.config"

export function ThemeDialog() {
    const { isOpen, onClose } = useThemeDialog()
    const { theme: activeTheme, setTheme } = useThemeStore()

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
            <DialogContent className="">
                <DialogHeader>
                    <DialogTitle>Appearance</DialogTitle>
                    <DialogDescription>Change how the application looks in your browser</DialogDescription>
                </DialogHeader>
                <Separator />
                <div>
                    <DialogTitle className="text-sm">Interface theme</DialogTitle>
                    <DialogDescription>Select your UI theme</DialogDescription>
                </div>
                <div className="grid grid-cols-3 gap-4">
                    {options.map((option) => (
                        <ThemeCard
                            key={option.theme}
                            active={activeTheme === option.theme}
                            label={option.label}
                            src={option.src}
                            onClick={() => setTheme(option.theme)}
                        />
                    ))}
                </div>
            </DialogContent>
        </Dialog>
    )
}
