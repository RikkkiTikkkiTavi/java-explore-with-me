package ru.practicum.server.user;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.user.dto.NewUserRequest;
import ru.practicum.server.user.dto.UserDto;
import ru.practicum.server.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/users")
@AllArgsConstructor
@Validated
public class UserController {

    private UserService service;

    @GetMapping
    public List<UserDto> getUsers(@RequestParam(defaultValue = "0") int from,
                                  @RequestParam(defaultValue = "10") int size,
                                  @RequestParam(required = false) List<Integer> ids) {
        return service.getUsers(ids, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@RequestBody @Valid NewUserRequest newUserRequest) {
        return service.create(newUserRequest);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{userId}")
    public void delete(@PathVariable("userId") int userId) {
        service.delete(userId);
    }
}
