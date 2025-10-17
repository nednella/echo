import { ThemeProvider } from "@/libs/theme/theme-provider"
import { EchoClerkProvider } from "@/providers/clerk-provider"
import { DialogProvider } from "@/providers/dialog-provider"
import { EchoQueryClientProvider } from "@/providers/query-client-provider"
import { SonnerProvider } from "@/providers/sonner-provider"

interface Props {
    children: React.ReactNode
}

export function AppProvider({ children }: Readonly<Props>) {
    return (
        <EchoClerkProvider>
            <EchoQueryClientProvider>
                {children}
                <ThemeProvider />
                <SonnerProvider />
                <DialogProvider />
            </EchoQueryClientProvider>
        </EchoClerkProvider>
    )
}
