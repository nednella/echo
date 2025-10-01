import { QueryClientProvider } from "@tanstack/react-query"

import { queryClient } from "@/utils/api"

interface Props {
    children: React.ReactNode
}

export function EchoQueryClientProvider({ children }: Readonly<Props>) {
    return <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
}
