package ru.kolpakovee.userservice.models.users;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class GetUserResponse {

    @NotNull
    String id;

    @NotEmpty
    String username;

    @Email
    String email;

    @NotEmpty
    String firstName;

    @NotEmpty
    String lastName;

    @Nullable
    String profilePictureUrl;

    long createdAt;
}
