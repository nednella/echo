import { StrictMode } from "react"
import { createRoot } from "react-dom/client"

import { App } from "./app/app"
import "./main.css"

createRoot(document.querySelector("#root")!).render(
    <StrictMode>
        <App />
    </StrictMode>
)
