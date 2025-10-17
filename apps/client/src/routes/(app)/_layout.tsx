import { Outlet, createFileRoute, redirect } from "@tanstack/react-router"

import { AppSidebar } from "@/components/sidebar/sidebar"
import { ScrollArea } from "@/libs/ui/components/scroll-area"
import { SidebarProvider } from "@/libs/ui/components/sidebar"
import { isAuthenticated, isOnboarded } from "@/utils/auth"

/**
 * Routes nested within the `/(app)` pathless layout should only be accessible
 * when the user **IS BOTH** authenticated and onboarded, otherwise the user should
 * be redirected accordingly.
 */
export const Route = createFileRoute("/(app)")({
    beforeLoad({ context: { auth }, location }) {
        if (!isAuthenticated(auth)) {
            throw redirect({
                to: "/login",
                replace: true,
                search: {
                    redirect: location.href
                }
            })
        }
        if (!isOnboarded(auth)) {
            throw redirect({
                to: "/onboarding",
                replace: true
            })
        }
    },
    component: AppLayout
})

function AppLayout() {
    return (
        <SidebarProvider className="grid h-screen w-screen p-2 md:grid-cols-[max-content_1fr] md:pl-0">
            <AppSidebar />
            <ScrollArea className="bg-sidebar relative overflow-y-auto rounded-lg border shadow-sm">
                <Outlet />
            </ScrollArea>
        </SidebarProvider>
    )
}
