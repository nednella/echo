import { Layout } from "../../../features/auth/layout/layout"
import { createFileRoute, Outlet } from "@tanstack/react-router"

export const Route = createFileRoute("/(public)/(auth)")({
    component: AuthLayout
})

function AuthLayout() {
    return (
        <Layout>
            <Outlet />
        </Layout>
    )
}
