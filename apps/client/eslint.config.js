import js from "@eslint/js"
import eslintPluginQuery from "@tanstack/eslint-plugin-query"
import eslintPluginRouter from "@tanstack/eslint-plugin-router"
import eslintConfigPrettier from "eslint-config-prettier"
import eslintPluginImport from "eslint-plugin-import"
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
            eslintPluginImport.flatConfigs.recommended,
            eslintPluginImport.flatConfigs.typescript,
            eslintPluginQuery.configs["flat/recommended"],
            eslintPluginRouter.configs["flat/recommended"],
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
            },
            "import/resolver": {
                typescript: true
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
                        env: true,
                        Utils: true,
                        utils: true
                    }
                }
            ],
            "import/no-unresolved": "off", // does not support Vite natively, see: https://github.com/import-js/eslint-plugin-import/blob/v2.32.0/docs/rules/no-unresolved.md#when-not-to-use-it\
            "import/no-default-export": "error", // disable default exporting
            "import/no-restricted-paths": [
                "error",
                {
                    basePath: "./src",
                    zones: [
                        // see: https://github.com/alan2207/bulletproof-react/blob/master/docs/project-structure.md

                        // target — director(y/ies) under scrutiny
                        // from — director(y/ies) that target(s) CANNOT depend on

                        // enforce unidirectional codebase:
                        {
                            // 1) shared layers should not depend on higher-level application layers

                            target: ["components", "hooks", "libs", "stores", "types", "utils"],
                            from: ["features", "routes"],
                            message: "❌ common/libs are shared layers — they should not import from features/routes!"
                        },
                        {
                            // 2) features should not depend on routes

                            target: "features",
                            from: "routes",
                            message: "❌ features are domain-specific modules — they should not import from routes!"
                        },

                        // disable cross-feature imports:
                        {
                            target: "features/auth",
                            from: "features",
                            except: ["auth"],
                            message: "❌ features are independent modules — they should not import from other features!"
                        },
                        {
                            target: "features/landing",
                            from: "features",
                            except: ["landing"],
                            message: "❌ features are independent modules — they should not import from other features!"
                        },
                        {
                            target: "features/onboarding",
                            from: "features",
                            except: ["onboarding"],
                            message: "❌ features are independent modules — they should not import from other features!"
                        }
                    ]
                }
            ]
        }
    }
])
