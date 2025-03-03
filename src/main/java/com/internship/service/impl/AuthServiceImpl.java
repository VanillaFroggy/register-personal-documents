package com.internship.service.impl;

import com.internship.persistence.entity.User;
import com.internship.persistence.repo.DocumentTypeRepository;
import com.internship.persistence.repo.UserRepository;
import com.internship.service.AuthService;
import com.internship.service.DocumentGroupService;
import com.internship.service.DocumentService;
import com.internship.service.dto.auth.RegisterDto;
import com.internship.service.dto.document.CreateDocumentDto;
import com.internship.service.dto.group.CreateDocumentGroupDto;
import com.internship.service.mapper.ServiceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final DocumentTypeRepository typeRepository;
    private final DocumentService documentService;
    private final DocumentGroupService documentGroupService;
    private final ServiceMapper mapper;

    @Override
    public void register(RegisterDto dto) {
        if (userRepository.findByUsername(dto.username()).isPresent()) {
            throw new IllegalArgumentException("Username is already in use");
        }
        User user = mapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        documentService.addDocument(new CreateDocumentDto(
                "Пароль",
                typeRepository.findByName("Пароль")
                        .orElseThrow(NullPointerException::new)
                        .getId(),
                documentGroupService.addGroup(new CreateDocumentGroupDto(
                        "All",
                        "#FFFFFF"
                )).id(),
                ZonedDateTime.now(ZoneOffset.UTC).plusDays(60)
        ));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(NullPointerException::new);
    }
}
