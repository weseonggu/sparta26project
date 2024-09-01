package com.sparta26.baemin.dto.common;

import com.querydsl.core.annotations.QueryProjection;
import com.sparta26.baemin.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor

public class AuditDto implements Serializable {

    private LocalDateTime createdAt;

    private String createdBy;

    private LocalDateTime updatedAt;

    private String updatedBy;

    private LocalDateTime deletedAt;

    private String deletedBy;

    private boolean isPublic;


    public AuditDto(Member member) {
        this.createdAt = member.getCreatedAt();
        this.createdBy = member.getCreatedBy();
        this.updatedAt = member.getUpdatedAt();
        this.updatedBy = member.getUpdatedBy();
        this.deletedAt = member.getDeletedAt();
        this.deletedBy = member.getDeletedBy();
        this.isPublic = member.isPublic();
    }
    @QueryProjection
    public AuditDto(LocalDateTime createdAt, String createdBy, LocalDateTime updatedAt, String updatedBy, LocalDateTime deletedAt, String deletedBy, boolean isPublic){
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
        this.deletedAt = deletedAt;
        this.deletedBy = deletedBy;
        this.isPublic = isPublic;
    }
}
