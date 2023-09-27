package ru.practicum.server.user.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.server.user.dto.NewUserRequest;
import ru.practicum.server.user.dto.UserDto;
import ru.practicum.server.user.mapper.UserMapper;
import ru.practicum.server.user.model.User;
import ru.practicum.server.user.repository.UserRepository;
import ru.practicum.server.user.validator.UserValidator;

import java.util.List;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserDto> getUsers(List<Integer> ids, int from, int size) {
        PageRequest pr = PageRequest.of(from, size, Sort.by(Sort.Direction.ASC, "id"));
        if (ids == null) {
            return UserMapper.toUserDtoList(userRepository.findAll(pr));
        }
        return UserMapper.toUserDtoList(userRepository.findAllByIdIn(ids, pr));
    }

    @Transactional
    @Override
    public UserDto create(NewUserRequest newUserRequest) {
        UserValidator.checkUser(newUserRequest);
       User newUser = userRepository.save(UserMapper.newUserRequestToUser(newUserRequest));
        return UserMapper.userToUserDto(newUser);
    }

    @Transactional
    @Override
    public void delete(int userId) {
        userRepository.deleteById(userId);
    }
}
