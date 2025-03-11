package com.internship.persistence.repo;

import com.internship.persistence.entity.DocumentGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentGroupRepository extends JpaRepository<DocumentGroup, Long> {
    List<DocumentGroup> findAllByUserId(Long userId);

    Page<DocumentGroup> findAllByUserId(Long userId, Pageable pageable);
}
