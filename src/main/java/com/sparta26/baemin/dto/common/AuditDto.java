package com.sparta26.baemin.dto.common;

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
@AllArgsConstructor
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
}
