package com.internship.persistence.repo.impl;

import com.internship.persistence.entity.Document;
import com.internship.persistence.entity.QDocument;
import com.internship.persistence.repo.DocumentRepositoryCustom;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DocumentRepositoryCustomImpl implements DocumentRepositoryCustom {
    private final EntityManager em;

    @Override
    public Page<Document> findAllByUserId(Long userId, Pageable pageable) {
        List<Document> documents = new JPAQuery<Document>(em)
                .select(QDocument.document)
                .from(QDocument.document)
                .where(QDocument.document.user.id.eq(userId))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();
        return new PageImpl<>(documents, pageable, documents.size());
    }

    @Override
    public Page<Document> findAllByUserIdAndDocumentGroupId(Long userId, Long documentGroupId, Pageable pageable) {
        List<Document> documents = new JPAQuery<Document>(em)
                .select(QDocument.document)
                .from(QDocument.document)
                .where(QDocument.document.user.id.eq(userId)
                        .and(QDocument.document.documentGroup.id.eq(documentGroupId)))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .orderBy(QDocument.document.dateOfIssue.desc())
                .fetch();
        return new PageImpl<>(documents, pageable, documents.size());
    }
}
