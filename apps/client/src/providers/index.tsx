import { EchoClerkProvider } from "./clerk-provider"
import { EchoQueryClientProvider } from "./query-client-provider"
import SonnerProvider from "./sonner-provider"

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
