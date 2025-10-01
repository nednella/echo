import { EchoClerkProvider } from "./clerk-provider"
import { EchoQueryClientProvider } from "./query-client-provider"

interface Props {
    children: React.ReactNode
}

export function AppProvider({ children }: Readonly<Props>) {
    return (
        <EchoClerkProvider>
            <EchoQueryClientProvider>{children}</EchoQueryClientProvider>
        </EchoClerkProvider>
    )
}
