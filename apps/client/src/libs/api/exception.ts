import type { schemas } from "./openapi-client"

class ApiException extends Error {
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

export { ApiException }
