import { Outlet, createFileRoute } from "@tanstack/react-router"

import { Layout } from "@/features/auth/layout/layout"

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
