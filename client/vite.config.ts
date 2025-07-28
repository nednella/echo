import tailwindcss from "@tailwindcss/vite"
import { tanstackRouter } from "@tanstack/router-plugin/vite"
import react from "@vitejs/plugin-react"
import { defineConfig } from "vite"

// https://vite.dev/config/
export default defineConfig({
    plugins: [
        tanstackRouter({
            target: "react",
            routeToken: "_layout",
            autoCodeSplitting: true
        }),
        react(),
        tailwindcss()
    ]
})
