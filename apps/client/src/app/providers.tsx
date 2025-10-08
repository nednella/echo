import { EchoClerkProvider } from "@/providers/clerk-provider"
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
                <SonnerProvider />
            </EchoQueryClientProvider>
        </EchoClerkProvider>
    )
}
