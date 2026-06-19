import { useEffect, useState } from "react"

import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query"
import { toast } from "sonner"

import { currentProfileQueryOptions, updateProfileMutationOptions } from "@/features/profile/api/options"
import { ApiException } from "@/libs/api/exception"
import { Button } from "@/libs/ui/components/button"
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle } from "@/libs/ui/components/dialog"
import { Input } from "@/libs/ui/components/input"
import { Textarea } from "@/libs/ui/components/textarea"
import { useUpdateProfileDialog } from "@/stores/update-profile-dialog.store"

export function UpdateProfileDialog() {
    const { isOpen, onClose } = useUpdateProfileDialog()
    const queryClient = useQueryClient()

    const { data: profile } = useQuery(currentProfileQueryOptions())

    const [name, setName] = useState(profile?.name ?? "")
    const [bio, setBio] = useState(profile?.bio ?? "")
    const [location, setLocation] = useState(profile?.location ?? "")

    useEffect(() => {
        if (isOpen) {
            setName(profile?.name ?? "")
            setBio(profile?.bio ?? "")
            setLocation(profile?.location ?? "")
        }
    }, [isOpen, profile])

    const { mutate, isPending } = useMutation({
        ...updateProfileMutationOptions(),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["profile"] })
            onClose()
        },
        onError: (error) => {
            const description = error instanceof ApiException ? error.message : "Please try again later"
            toast.error("Could not update profile", { description })
        }
    })

    const handleSave = () => {
        mutate({
            name: name.trim(),
            bio: bio.trim(),
            location: location.trim()
        })
    }

    const onOpenChange = (open: boolean) => {
        if (!open) onClose()
    }

    return (
        <Dialog
            open={isOpen}
            onOpenChange={onOpenChange}
        >
            <DialogContent className="sm:max-w-md">
                <DialogHeader>
                    <DialogTitle>Edit profile</DialogTitle>
                    <DialogDescription>Update your name, bio, and location.</DialogDescription>
                </DialogHeader>

                <div className="flex flex-col gap-4">
                    <div className="flex flex-col gap-1.5">
                        <label
                            htmlFor="profile-name"
                            className="text-sm font-medium"
                        >
                            Name
                        </label>
                        <Input
                            id="profile-name"
                            value={name}
                            onChange={(event) => setName(event.target.value)}
                            placeholder="Your name"
                        />
                    </div>

                    <div className="flex flex-col gap-1.5">
                        <label
                            htmlFor="profile-bio"
                            className="text-sm font-medium"
                        >
                            Bio
                        </label>
                        <Textarea
                            id="profile-bio"
                            value={bio}
                            onChange={(event) => setBio(event.target.value)}
                            placeholder="Tell people a bit about yourself"
                            rows={3}
                        />
                    </div>

                    <div className="flex flex-col gap-1.5">
                        <label
                            htmlFor="profile-location"
                            className="text-sm font-medium"
                        >
                            Location
                        </label>
                        <Input
                            id="profile-location"
                            value={location}
                            onChange={(event) => setLocation(event.target.value)}
                            placeholder="Where are you based?"
                        />
                    </div>

                    <div className="flex justify-end">
                        <Button
                            onClick={handleSave}
                            disabled={isPending}
                        >
                            {isPending ? "Saving…" : "Save"}
                        </Button>
                    </div>
                </div>
            </DialogContent>
        </Dialog>
    )
}
