import { createFileRoute, Outlet } from "@tanstack/react-router"

export const Route = createFileRoute("/_public")({
    component: PublicWrapper
})

function PublicWrapper() {
    return <Outlet />
}
