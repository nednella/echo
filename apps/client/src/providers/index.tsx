import { AuthProvider } from "./auth-provider"
import { EchoQueryClientProvider } from "./query-client-provider"

interface Props {
    children: React.ReactNode
}

export function AppProvider({ children }: Readonly<Props>) {
    return (
        <AuthProvider>
            <EchoQueryClientProvider>{children}</EchoQueryClientProvider>
        </AuthProvider>
    )
}
