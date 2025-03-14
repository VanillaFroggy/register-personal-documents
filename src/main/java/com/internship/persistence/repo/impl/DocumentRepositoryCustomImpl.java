package com.internship.persistence.repo.impl;

import com.internship.persistence.entity.Document;
import com.internship.persistence.entity.QDocument;
import com.internship.persistence.repo.DocumentRepositoryCustom;
import com.querydsl.core.Fetchable;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DocumentRepositoryCustomImpl implements DocumentRepositoryCustom {
    private final EntityManager entityManager;

    @Override
    public Page<Document> findAllByUserId(Long userId, Pageable pageable) {
        return new PageImpl<>(
                new JPAQuery<Document>(entityManager)
                        .select(QDocument.document)
                        .from(QDocument.document)
                        .where(QDocument.document.user.id.eq(userId))
                        .limit(pageable.getPageSize())
                        .offset(pageable.getOffset())
                        .fetch(),
                pageable,
                new JPAQuery<Document>(entityManager)
                        .from(QDocument.document)
                        .where(QDocument.document.user.id.eq(userId))
                        .transform(Fetchable::fetchCount)
        );
    }

    @Override
    public Page<Document> findAllByUserIdAndDocumentGroupId(Long userId, Long documentGroupId, Pageable pageable) {
        return new PageImpl<>(
                new JPAQuery<Document>(entityManager)
                        .select(QDocument.document)
                        .from(QDocument.document)
                        .where(QDocument.document.user.id.eq(userId)
                                .and(QDocument.document.documentGroup.id.eq(documentGroupId)))
                        .limit(pageable.getPageSize())
                        .offset(pageable.getOffset())
                        .orderBy(QDocument.document.dateOfIssue.desc())
                        .fetch(),
                pageable,
                new JPAQuery<Document>(entityManager)
                        .from(QDocument.document)
                        .where(QDocument.document.user.id.eq(userId)
                                .and(QDocument.document.documentGroup.id.eq(documentGroupId)))
                        .transform(Fetchable::fetchCount)
        );
    }
}
