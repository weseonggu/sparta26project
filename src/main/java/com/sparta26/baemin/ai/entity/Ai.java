package com.sparta26.baemin.ai.entity;

import com.sparta26.baemin.common.entity.AuditEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "p_AI")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ai extends AuditEntity {

    @Id
    @GeneratedValue @UuidGenerator
    @Column(name = "ai_id")
    private UUID id;

    private String question;
    private String answer;

    public Ai(String question, String answer, String username) {
        this.question = question;
        this.answer = answer;
        super.addCreatedBy(username);
    }
}
