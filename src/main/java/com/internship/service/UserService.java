package com.internship.service;

import com.internship.service.dto.user.UserDto;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public interface UserService {
    UserDto getUserById(@NotNull Long id);

    void deleteUserById(@NotNull Long id);
}
