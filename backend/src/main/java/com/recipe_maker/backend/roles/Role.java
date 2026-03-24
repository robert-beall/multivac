package com.recipe_maker.backend.roles;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity class representing a user role.
 */
@AllArgsConstructor
@Data
@Entity
@NoArgsConstructor
@Table(name = "roles")
public class Role {
    /** The unique identifier for the role. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The name of the role. */
    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false, updatable = false)
    private RoleName name; 
}
