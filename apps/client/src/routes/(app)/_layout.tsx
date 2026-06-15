import { Outlet, createFileRoute, redirect } from "@tanstack/react-router"

import { ScrollArea } from "@/libs/ui/components/scroll-area"
import { isAuthenticated, isOnboarded } from "@/utils/auth"

import { TopNav } from "./-components/topnav"

/**
 * Routes nested within the `/(app)` pathless route group should only be accessible
 * when the user **IS BOTH** authenticated and onboarded, otherwise the user should
 * be redirected accordingly.
 *
 * docs: https://tanstack.com/router/v1/docs/framework/react/routing/routing-concepts#pathless-route-group-directories
 *
 * note: this should not be possible using pathless route groups, but it is: https://github.com/TanStack/router/discussions/5530
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
        <div className="bg-background text-foreground flex h-screen flex-col">
            <TopNav />
            <ScrollArea className="min-h-0 flex-1">
                <Outlet />
            </ScrollArea>
        </div>
    )
}
