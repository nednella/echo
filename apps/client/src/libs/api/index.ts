import createOpenApiClient from "openapi-fetch"

import type { components, paths } from "./v1"

export const createClient = (baseUrl: string) => createOpenApiClient<paths>({ baseUrl })

export type { paths, operations } from "./v1"
export type schemas = components["schemas"]
