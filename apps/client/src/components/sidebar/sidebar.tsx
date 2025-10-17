import { Sidebar, SidebarContent, SidebarFooter, SidebarHeader } from "@/libs/ui/components/sidebar"

import { Footer } from "./sidebar-footer"
import { Header } from "./sidebar-header"
import { Navigation } from "./sidebar-nav"

export function AppSidebar() {
    return (
        <Sidebar
            variant="floating"
            collapsible="icon"
        >
            <SidebarHeader>
                <Header />
            </SidebarHeader>
            <SidebarContent>
                <Navigation />
            </SidebarContent>
            <SidebarFooter>
                <Footer />
            </SidebarFooter>
        </Sidebar>
    )
}
