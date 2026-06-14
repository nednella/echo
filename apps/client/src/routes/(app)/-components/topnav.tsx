import { useUser } from "@clerk/clerk-react"
import { Link } from "@tanstack/react-router"
import { Bell, House, type LucideIcon, Search, UserRound } from "lucide-react"

import { EchoLogo } from "@/components/logos/echo-logo"
import { AccountMenu } from "@/features/account/components/account-dropdown"
import { useThemeStore } from "@/libs/theme/theme.store"
import { resolveTheme } from "@/libs/theme/utils"
import { Avatar, AvatarFallback, AvatarImage } from "@/libs/ui/components/avatar"
import { Button } from "@/libs/ui/components/button"

const navBase = "grid size-10 cursor-pointer place-items-center rounded-lg transition-colors"
const navInactive = "text-muted-foreground hover:bg-accent hover:text-foreground"
const navActive =
    "from-echo-400 to-echo-600 bg-gradient-to-br text-white shadow-[0_4px_14px_-5px_var(--color-echo-500)]"

/** A nav destination that routes to a real page, with a gradient-pill active state. */
function NavLink({ to, label, icon: Icon }: { to: string; label: string; icon: LucideIcon }) {
    return (
        <Link
            to={to}
            aria-label={label}
            title={label}
            className={navBase}
            activeProps={{ className: navActive }}
            inactiveProps={{ className: navInactive }}
        >
            <Icon className="size-5" />
        </Link>
    )
}

/** A placeholder nav entry for a surface that doesn't exist yet. */
function NavComingSoon({ label, icon: Icon }: { label: string; icon: LucideIcon }) {
    return (
        <button
            type="button"
            aria-label={label}
            title={label}
            className={`${navBase} ${navInactive}`}
        >
            <Icon className="size-5" />
        </button>
    )
}

/** Top-right avatar that opens the account menu (profile, appearance, log out). */
function AccountAvatar() {
    const { user } = useUser()

    return (
        <AccountMenu align="end">
            <Button
                size="avatar"
                variant="ghost"
                className="data-[state=open]:bg-accent"
            >
                <Avatar className="size-8">
                    <AvatarImage
                        src={user?.imageUrl}
                        alt={user?.username ?? undefined}
                    />
                    <AvatarFallback>
                        <UserRound size={16} />
                    </AvatarFallback>
                </Avatar>
            </Button>
        </AccountMenu>
    )
}

export function TopNav() {
    const { theme } = useThemeStore()
    const logoVariant = resolveTheme(theme) === "dark" ? "light-gradient" : "gradient"

    return (
        <header
            className="bg-background/80 sticky top-0 z-20 grid h-14 grid-cols-[1fr_auto_1fr] items-center gap-4 border-b
                px-4 backdrop-blur-md md:px-6"
        >
            {/* accent: gradient hairline under the bar */}
            <div
                className="via-echo-400/50 pointer-events-none absolute inset-x-0 -bottom-px h-px bg-gradient-to-r
                    from-transparent to-transparent"
            />

            <Link
                to="/home"
                className="flex w-fit items-center gap-2 text-lg font-semibold tracking-tight"
            >
                <EchoLogo
                    variant={logoVariant}
                    size={24}
                />
                echo
            </Link>

            <nav className="flex items-center gap-1 justify-self-center">
                <NavLink
                    to="/home"
                    label="Home"
                    icon={House}
                />
                <NavComingSoon
                    label="Search"
                    icon={Search}
                />
                <NavComingSoon
                    label="Notifications"
                    icon={Bell}
                />
            </nav>

            <div className="flex items-center justify-self-end">
                <AccountAvatar />
            </div>
        </header>
    )
}
