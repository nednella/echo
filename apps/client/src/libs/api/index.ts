import createOpenApiClient from "openapi-fetch"

import type { components, paths } from "./v1"

export const createClient = (baseUrl: string) => createOpenApiClient<paths>({ baseUrl })

export class ApiException extends Error {
    public timestamp: string
    public status: number
    public path: string
    public response: Response

    constructor(error: schemas["ErrorResponse"], response: Response) {
        super(error.message)
        this.timestamp = error.timestamp
        this.status = error.status
        this.path = error.path
        this.response = response
        this.name = "ApiException"
    }
}

export type { Middleware } from "openapi-fetch"
export type { operations, paths } from "./v1"
export type schemas = components["schemas"]
export type Client = ReturnType<typeof createClient>
