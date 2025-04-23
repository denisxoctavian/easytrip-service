package com.easytrip.backend.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Data
@Table(name="app_deal")
public class Deal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "label", nullable = false)
    private String label;

    @Column(name = "link", nullable = false)
    private String link;

    @Column(name = "image", nullable = false)
    private String image;

    @Column(name = "active", nullable = false)
    private Boolean active;

}
