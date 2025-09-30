import js from "@eslint/js"
import eslintConfigPrettier from "eslint-config-prettier"
import reactHooks from "eslint-plugin-react-hooks"
import reactRefresh from "eslint-plugin-react-refresh"
import eslintPluginUnicorn from "eslint-plugin-unicorn"
import { globalIgnores } from "eslint/config"
import globals from "globals"
import tseslint from "typescript-eslint"

export default tseslint.config([
    globalIgnores(["dist", "src/routeTree.gen.ts", "CHANGELOG.md", "src/libs/api/v1.d.ts"]),
    {
        files: ["**/*.{ts,tsx}"],
        extends: [
            js.configs.recommended,
            tseslint.configs.recommended,
            eslintPluginUnicorn.configs.recommended,
            reactHooks.configs["recommended-latest"],
            reactRefresh.configs.vite,
            eslintConfigPrettier // must be last
        ],
        languageOptions: {
            ecmaVersion: 2020,
            globals: globals.browser
        },
        settings: {
            react: {
                version: "detect"
            }
        },
        rules: {
            "unicorn/prevent-abbreviations": [
                "error",
                {
                    allowList: {
                        Props: true,
                        props: true,
                        Env: true,
                        env: true
                    }
                }
            ]
        }
    }
])
