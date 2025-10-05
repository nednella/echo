import { mutationOptions } from "@tanstack/react-query"

import { client } from "@/utils/api"
import { delayWithPromise } from "@/utils/delay"

export const onboardingMutationOptions = (minimumDelayMs: number) =>
    mutationOptions({
        mutationFn: async () => {
            // API call is forced to a minimum delay for UX
            const [response] = await Promise.allSettled([
                client.POST("/v1/clerk/onboarding"),
                delayWithPromise(minimumDelayMs)
            ])

            // The mutation will resolve to success if the API call status is not checked & explicity thrown in cases of rejection
            if (response.status === "rejected") throw response.reason
            return response.value.data
        }
    })
