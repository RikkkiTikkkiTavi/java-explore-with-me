package ru.practicum.server.user.service;

import ru.practicum.server.user.dto.NewUserRequest;
import ru.practicum.server.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers(List<Integer> ids, int from, int size);
    UserDto create(NewUserRequest newUserRequest);
    void delete(int userId);
}
