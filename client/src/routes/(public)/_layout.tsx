import { createFileRoute, Outlet } from "@tanstack/react-router"

// (public) layout component
export const Route = createFileRoute("/(public)")({
    component: Layout
})

function Layout() {
    return <Outlet />
}
