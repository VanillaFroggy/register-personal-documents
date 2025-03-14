package com.internship.persistence.repo;

import com.internship.persistence.entity.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DocumentRepositoryCustom {
    Page<Document> findAllByUserId(Long userId, Pageable pageable);

    Page<Document> findAllByUserIdAndDocumentGroupId(Long userId, Long documentGroupId, Pageable pageable);
}
