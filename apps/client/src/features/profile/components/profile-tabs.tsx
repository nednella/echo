import { Link } from "@tanstack/react-router"

type ProfileTabsProps = Readonly<{
    username: string
}>

const tabs = [
    { to: "/profile/$username" as const, label: "Posts", exact: true },
    { to: "/profile/$username/replies" as const, label: "Replies", exact: false },
    { to: "/profile/$username/likes" as const, label: "Likes", exact: false },
    { to: "/profile/$username/mentions" as const, label: "Mentions", exact: false }
]

export function ProfileTabs({ username }: ProfileTabsProps) {
    return (
        <nav className="bg-background/80 sticky top-0 z-10 flex border-b backdrop-blur-md">
            {tabs.map((tab) => (
                <Link
                    key={tab.to}
                    to={tab.to}
                    params={{ username }}
                    activeOptions={{ exact: tab.exact }}
                    className="group text-muted-foreground data-[status=active]:text-foreground relative flex flex-1
                        items-center justify-center py-4 text-sm font-medium transition-colors"
                >
                    {tab.label}
                    <span
                        className="from-echo-400 to-echo-600 absolute bottom-0 h-0.5 w-full rounded-full bg-linear-to-r
                            opacity-0 transition-opacity group-data-[status=active]:opacity-100"
                    />
                </Link>
            ))}
        </nav>
    )
}
