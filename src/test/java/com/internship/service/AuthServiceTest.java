package com.internship.service;

import com.internship.persistence.entity.Document;
import com.internship.persistence.entity.DocumentGroup;
import com.internship.persistence.entity.DocumentType;
import com.internship.persistence.entity.User;
import com.internship.persistence.repo.DocumentGroupRepository;
import com.internship.persistence.repo.DocumentRepository;
import com.internship.persistence.repo.DocumentTypeRepository;
import com.internship.persistence.repo.UserRepository;
import com.internship.service.dto.auth.RegisterDto;
import com.internship.service.dto.document.CreateDocumentDto;
import com.internship.service.dto.group.CreateDocumentGroupDto;
import com.internship.service.exceptoin.NotFoundException;
import com.internship.service.impl.AuthServiceImpl;
import com.internship.service.mapper.ServiceMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String TYPE_NAME = "Пароль";

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private DocumentGroupRepository groupRepository;

    @Mock
    private DocumentTypeRepository typeRepository;

    @Mock
    private ServiceMapper mapper;

    @Test
    void register_shouldReturnNothing_whenSuccessfullyRegistered() throws NotFoundException {
        RegisterDto dto = mock(RegisterDto.class);
        User user = mock(User.class);

        when(mapper.toEntity(dto)).thenReturn(user);
        when(user.getPassword()).thenReturn(PASSWORD);
        when(passwordEncoder.encode(user.getPassword())).thenReturn(PASSWORD);
        when(userRepository.save(user)).thenReturn(user);
        when(mapper.toEntity(any(CreateDocumentGroupDto.class))).thenReturn(mock(DocumentGroup.class));
        when(mapper.toEntity(any(CreateDocumentDto.class))).thenReturn(mock(Document.class));
        when(typeRepository.findByName(TYPE_NAME)).thenReturn(Optional.of(mock(DocumentType.class)));

        authService.register(dto);

        verify(userRepository, times(1)).save(user);
        verify(groupRepository, times(1)).save(any(DocumentGroup.class));
        verify(documentRepository, times(1)).save(any(Document.class));
    }

    @Test
    void register_shouldThrowIllegalArgumentException_whenUsernameIsUsed() {
        RegisterDto dto = mock(RegisterDto.class);

        when(userRepository.findByUsername(dto.username())).thenReturn(Optional.of(mock(User.class)));

        assertThrows(IllegalArgumentException.class, () -> authService.register(dto));
    }

    @Test
    void register_shouldThrowNotFoundException_whenPasswordTypeDoesNotExist() {
        RegisterDto dto = mock(RegisterDto.class);
        User user = mock(User.class);

        when(mapper.toEntity(dto)).thenReturn(user);
        when(user.getPassword()).thenReturn(PASSWORD);
        when(passwordEncoder.encode(user.getPassword())).thenReturn(PASSWORD);
        when(userRepository.save(user)).thenReturn(user);
        when(mapper.toEntity(any(CreateDocumentGroupDto.class))).thenReturn(mock(DocumentGroup.class));
        when(typeRepository.findByName(TYPE_NAME)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> authService.register(dto));
    }

    @Test
    void loadUserByUsername_shouldReturnUserDetails_whenExists() {
        User user = mock(User.class);

        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(user.getUsername()).thenReturn(USERNAME);

        UserDetails actual = authService.loadUserByUsername(USERNAME);

        assertNotNull(actual);
        assertEquals(USERNAME, actual.getUsername());
        verify(userRepository, times(1)).findByUsername(USERNAME);
    }

    @Test
    void loadUserByUsername_shouldThrowUsernameNotFoundException_whenDoesNotExist() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> authService.loadUserByUsername(USERNAME));
    }
}