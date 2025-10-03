import createOpenApiClient from "openapi-fetch"

import type { components, paths } from "./v1"

export const createClient = (baseUrl: string) => createOpenApiClient<paths>({ baseUrl })

export type { Middleware } from "openapi-fetch"
export type { operations, paths } from "./v1"
export type schemas = components["schemas"]
export type Client = ReturnType<typeof createClient>
