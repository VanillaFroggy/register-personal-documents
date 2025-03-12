package com.internship.persistence.repo;

import com.internship.config.JpaTestConfig;
import com.internship.persistence.entity.DocumentGroup;
import com.internship.persistence.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {JpaTestConfig.class})
class DocumentGroupRepositoryTest {
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String GROUP_NAME = "groupName";
    private static final String GROUP_COLOR = "groupColor";

    @Autowired
    private DocumentGroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        groupRepository.deleteAll();
        groupRepository.flush();
        userRepository.deleteAll();
        userRepository.flush();
    }

    @Test
    void findListOfDocumentGroupsByUserId_shouldFindDocumentGroups_whenExists() {
        final User user = new User();
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        final DocumentGroup group = new DocumentGroup();
        group.setName(GROUP_NAME);
        group.setColor(GROUP_COLOR);
        group.setUser(user);

        userRepository.save(user);
        groupRepository.save(group);
        final List<DocumentGroup> groups = groupRepository.findAllByUserId(user.getId());

        assertFalse(groups.isEmpty());
        assertNotNull(groups.getFirst());
        assertEquals(group, groups.getFirst());
    }

    @Test
    void findListOfDocumentGroupsByUserId_shouldFindNothing_whenDontExist() {
        final User user = new User();
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);

        final List<DocumentGroup> groups = groupRepository.findAllByUserId(user.getId());

        assertTrue(groups.isEmpty());
    }

    @Test
    void findPageOfDocumentGroupsByUserId_shouldFindDocumentGroups_whenExist() {
        final User user = new User();
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        final DocumentGroup group = new DocumentGroup();
        group.setName(GROUP_NAME);
        group.setColor(GROUP_COLOR);
        group.setUser(user);

        userRepository.save(user);
        groupRepository.save(group);
        final Page<DocumentGroup> groups = groupRepository.findAllByUserId(
                user.getId(),
                PageRequest.of(0, 10)
        );

        assertFalse(groups.isEmpty());
        assertNotNull(groups.getContent().getFirst());
        assertEquals(group, groups.getContent().getFirst());
    }

    @Test
    void findPageOfDocumentGroupsByUserId_shouldFindNothing_whenDontExist() {
        final User user = new User();
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);

        userRepository.save(user);
        final Page<DocumentGroup> groups = groupRepository.findAllByUserId(
                user.getId(),
                PageRequest.of(0, 10)
        );

        assertTrue(groups.isEmpty());
        assertTrue(groups.isLast());
    }
}