import { Link } from "@tanstack/react-router"

import { AccountAvatar } from "@/components/account-avatar"
import { EchoLogo } from "@/components/logos/echo-logo"
import { AccountMenu } from "@/features/account/components/account-dropdown"
import { Button } from "@/libs/ui/components/button"

import { navItems } from "./navbar.config"

const navBase = "grid size-10 place-items-center rounded-lg transition-colors"
const navInactive = "text-muted-foreground hover:bg-accent hover:text-foreground"
const navActive = "from-echo-400 to-echo-600 bg-linear-to-br text-white shadow-[0_4px_14px_-5px_var(--color-echo-500)]"

export function TopNav() {
    return (
        <header
            className="bg-background/80 sticky top-0 z-20 grid h-14 grid-cols-[1fr_auto_1fr] items-center gap-4 border-b
                px-4 backdrop-blur-md md:px-6"
        >
            {/* accent: gradient hairline under the bar */}
            <div
                className="via-echo-400/50 pointer-events-none absolute inset-x-0 -bottom-px h-px bg-linear-to-r
                    from-transparent to-transparent"
            />

            <Link
                to="/home"
                className="flex w-fit items-center gap-2 text-lg font-semibold tracking-tight"
            >
                <EchoLogo
                    variant="light-gradient"
                    size={24}
                />
                echo
            </Link>

            <nav className="flex items-center gap-1 justify-self-center">
                {navItems.map((item) => (
                    <Link
                        key={item.label}
                        to={item.to}
                        aria-label={item.label}
                        title={item.label}
                        className={navBase}
                        activeProps={{ className: navActive }}
                        inactiveProps={{ className: navInactive }}
                    >
                        <item.icon className="size-5" />
                    </Link>
                ))}
            </nav>

            <div className="flex items-center justify-self-end">
                <AccountMenu align="end">
                    <Button
                        size="avatar"
                        variant="ghost"
                        className="data-[state=open]:bg-accent"
                    >
                        <AccountAvatar />
                    </Button>
                </AccountMenu>
            </div>
        </header>
    )
}
