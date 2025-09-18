/// <reference types="vite/client" />

interface ImportMetaEnv {
    readonly VITE_CLERK_PUBLISHABLE_KEY: string
    // add more VITE_* keys here...
}

interface ImportMeta {
    readonly env: ImportMetaEnv
}
