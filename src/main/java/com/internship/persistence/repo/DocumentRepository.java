package com.internship.persistence.repo;

import com.internship.persistence.entity.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    @Query("select d from Document as d where d.user.id = :userId")
    Page<Document> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("select d from Document as d where d.user.id = :userId and d.documentGroup.id = :documentGroupId" +
            " order by d.dateOfIssue desc")
    Page<Document> findAllByUserIdAndDocumentGroupId(
            @Param("userId") Long userId,
            @Param("documentGroupId") Long documentGroupId,
            Pageable pageable
    );
}
