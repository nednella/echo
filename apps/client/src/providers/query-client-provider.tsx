import { QueryClientProvider } from "@tanstack/react-query"

import { queryClient } from "@/libs/api/query-client"

type EchoQueryClientProviderProps = Readonly<{
    children: React.ReactNode
}>

export function EchoQueryClientProvider({ children }: EchoQueryClientProviderProps) {
    return <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
}
