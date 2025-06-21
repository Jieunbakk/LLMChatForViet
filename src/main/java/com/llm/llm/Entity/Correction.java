package com.llm.llm.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
public class Correction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int chathistoryId;

    @Lob
    @Column(columnDefinition = "LONGTEXT",nullable = false)
    private String correction;

    @CreationTimestamp
    private Timestamp createdAt;
}
