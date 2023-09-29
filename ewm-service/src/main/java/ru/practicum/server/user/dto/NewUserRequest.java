package ru.practicum.server.user.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Value
@Builder
public class NewUserRequest {
    @Email
    @NotNull
    @NotBlank
    @Size(min = 6, max = 254, message = "Name size must be less than 254")

    String email;

    @NotNull
    @NotBlank
    @Size(min = 2, max = 250, message = "Name size must be less than 250")
    String name;
}
