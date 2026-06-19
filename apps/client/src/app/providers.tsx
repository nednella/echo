import { ThemeProvider } from "@/libs/theme/theme-provider"
import { EchoClerkProvider } from "@/providers/clerk-provider"
import { EchoQueryClientProvider } from "@/providers/query-client-provider"
import { SonnerProvider } from "@/providers/sonner-provider"

type AppProviderProps = Readonly<{
    children: React.ReactNode
}>

export function AppProvider({ children }: AppProviderProps) {
    return (
        <EchoClerkProvider>
            <EchoQueryClientProvider>
                {children}
                <ThemeProvider />
                <SonnerProvider />
            </EchoQueryClientProvider>
        </EchoClerkProvider>
    )
}
