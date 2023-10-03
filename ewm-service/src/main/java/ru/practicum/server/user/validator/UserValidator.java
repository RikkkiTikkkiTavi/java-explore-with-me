package ru.practicum.server.user.validator;

import ru.practicum.server.user.dto.NewUserRequest;
import ru.practicum.server.exception.ValidateException;

public class UserValidator {
    public static void checkUser(NewUserRequest newUserRequest) {
        if (newUserRequest.getEmail().chars().allMatch(c -> c == 32)) {
            throw new ValidateException("Email cannot consist only of spaces");
        }

        if (newUserRequest.getName().chars().allMatch(c -> c == 32)) {
            throw new ValidateException("Name cannot consist only of spaces");
        }
    }
}
