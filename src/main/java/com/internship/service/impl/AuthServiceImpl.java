package com.internship.service.impl;

import com.internship.persistence.entity.Document;
import com.internship.persistence.entity.DocumentGroup;
import com.internship.persistence.entity.DocumentType;
import com.internship.persistence.entity.User;
import com.internship.persistence.repo.DocumentGroupRepository;
import com.internship.persistence.repo.DocumentRepository;
import com.internship.persistence.repo.DocumentTypeRepository;
import com.internship.persistence.repo.UserRepository;
import com.internship.service.AuthService;
import com.internship.service.dto.auth.RegisterDto;
import com.internship.service.dto.document.CreateDocumentDto;
import com.internship.service.dto.group.CreateDocumentGroupDto;
import com.internship.service.exceptoin.NotFoundException;
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
    private static final String DOCUMENT_TYPE_NAME = "Пароль";

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final DocumentGroupRepository groupRepository;
    private final DocumentTypeRepository typeRepository;
    private final DocumentRepository documentRepository;
    private final ServiceMapper mapper;

    @Override
    public void register(RegisterDto dto) throws NotFoundException {
        if (userRepository.findByUsername(dto.username()).isPresent()) {
            throw new IllegalArgumentException("Username is already in use");
        }
        User user = mapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        DocumentGroup group = mapper.toEntity(new CreateDocumentGroupDto("All", "#FFFFFF"));
        group.setUser(user);
        groupRepository.save(group);
        DocumentType type = typeRepository.findByName(DOCUMENT_TYPE_NAME)
                .orElseThrow(NotFoundException::new);
        Document document = mapper.toEntity(new CreateDocumentDto(
                DOCUMENT_TYPE_NAME,
                type.getId(),
                group.getId(),
                ZonedDateTime.now(ZoneOffset.UTC).plusDays(60)
        ));
        document.setUser(user);
        document.setDocumentGroup(group);
        document.setDocumentType(type);
        documentRepository.save(document);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with name " + username + "does not exist"));
    }
}
