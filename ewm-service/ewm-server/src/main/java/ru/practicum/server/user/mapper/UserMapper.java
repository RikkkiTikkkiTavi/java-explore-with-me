package ru.practicum.server.user.mapper;

import org.springframework.data.domain.Page;
import ru.practicum.server.user.dto.NewUserRequest;
import ru.practicum.server.user.dto.UserDto;
import ru.practicum.server.user.dto.UserShortDto;
import ru.practicum.server.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {
    public static User newUserRequestToUser(NewUserRequest newUserRequest) {
        return User.builder()
                .email(newUserRequest.getEmail())
                .name(newUserRequest.getName())
                .build();
    }

    public static UserDto userToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    public static UserShortDto userToUserShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public static List<UserDto> toUserDtoList(Page<User> users) {
        return users.stream().map(UserMapper::userToUserDto).collect(Collectors.toList());
    }
}
