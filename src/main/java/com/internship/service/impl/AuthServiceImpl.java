package com.internship.service.impl;

import com.internship.persistence.entity.Document;
import com.internship.persistence.entity.DocumentGroup;
import com.internship.persistence.entity.User;
import com.internship.persistence.repo.DocumentGroupRepository;
import com.internship.persistence.repo.DocumentRepository;
import com.internship.persistence.repo.DocumentTypeRepository;
import com.internship.persistence.repo.UserRepository;
import com.internship.service.AuthService;
import com.internship.service.dto.auth.RegisterDto;
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
    private final DocumentGroupRepository documentGroupRepository;
    private final DocumentTypeRepository typeRepository;
    private final DocumentRepository documentRepository;
    private final ServiceMapper mapper;

    @Override
    public void register(RegisterDto dto) {
        if (userRepository.findByUsername(dto.username()).isPresent()) {
            throw new IllegalArgumentException("Username is already in use");
        }
        User user = mapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        DocumentGroup documentGroup = DocumentGroup.builder()
                .name("All")
                .color("#FFFFFF")
                .user(user)
                .build();
        documentGroupRepository.save(documentGroup);
        Document document = Document.builder()
                .title("Пароль")
                .documentGroup(documentGroup)
                .documentType(typeRepository.findByName("Пароль")
                        .orElseThrow(NullPointerException::new))
                .user(user)
                .dateOfIssue(ZonedDateTime.now(ZoneOffset.UTC))
                .expirationDate(ZonedDateTime.now(ZoneOffset.UTC).plusDays(60))
                .build();
        documentRepository.save(document);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(NullPointerException::new);
    }
}
