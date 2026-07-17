package com.auth.demo.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

import com.github.f4b6a3.uuid.UuidCreator;

@Getter
@Setter
@MappedSuperclass
public abstract class SecureEntity extends AuditEntity {

    @Column(name = "public_id", unique = true, nullable = false, updatable = false)
    private UUID publicId;

    @PrePersist
    public void generateId() {
        if (publicId == null) {
            publicId = UuidCreator.getTimeOrderedEpoch();
        }
    }
}