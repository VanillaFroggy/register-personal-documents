package com.internship.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "document_groups")
@Getter
@EqualsAndHashCode
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String color;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "document_group_user_id", foreignKey = @ForeignKey(name = "document_group_user_id_fkey"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;
}
