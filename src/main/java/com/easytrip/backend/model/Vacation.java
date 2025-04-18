package com.easytrip.backend.model;


import com.easytrip.backend.enums.TravelCompanion;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "app_vacation")
public class Vacation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="country", nullable = false)
    private String country;

    @Column(name="startingDate", nullable = false)
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate startingDate;

    @Column(name="endingDate", nullable = false)
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate endingDate;

    @Column(name="days", nullable = false)
    private Integer days;

    @Column(name="budget", nullable = false)
    private BigDecimal budget;

    @Column(name="travel_companion", nullable = false)
    @Enumerated(EnumType.STRING)
    private TravelCompanion travelCompanion;

    @ManyToMany
    @JoinTable(
            name = "app_vacation_activities",
            joinColumns = @JoinColumn(name = "vacation_id"),
            inverseJoinColumns = @JoinColumn(name = "activity_id")
    )
    private List<Activity> activities;

    @OneToMany(mappedBy = "vacation", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ItineraryDay> itinerary;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
