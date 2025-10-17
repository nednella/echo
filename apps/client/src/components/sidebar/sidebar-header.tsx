import { Separator } from "@/libs/ui/components/separator"
import { SidebarHeader as SidebarHeaderUI, SidebarTrigger } from "@/libs/ui/components/sidebar"
import { Large } from "@/libs/ui/components/typography/large"

export function SidebarHeader() {
    return (
        <SidebarHeaderUI>
            <div className="flex h-12 items-center gap-2 overflow-hidden">
                <SidebarTrigger size="lg" />
                <Separator
                    orientation="vertical"
                    className="!h-8"
                />
                <Large className="text-accent-foreground pl-2">Navigation</Large>
            </div>
        </SidebarHeaderUI>
    )
}
