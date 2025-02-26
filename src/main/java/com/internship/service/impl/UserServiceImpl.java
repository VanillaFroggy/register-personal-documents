package com.internship.service.impl;

import com.internship.persistence.repo.UserRepository;
import com.internship.service.UserService;
import com.internship.service.dto.user.UserDto;
import com.internship.service.mapper.ServiceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ServiceMapper mapper;

    @Override
    public UserDto getUserById(Long id) {
        return userRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(NullPointerException::new);
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
}
