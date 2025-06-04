package com.vueart.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Getter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "exhibitionInfo")
public class ExhibitionInfo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(length = 255)
    private String title;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String summary;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull
    @Column(name = "start_date")
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date")
    private LocalDate endDate;

    @NotNull
    @Column(length = 255)
    private String location;

    @NotNull
    @Column(name = "is_presale")
    private Boolean isPresale;

    @NotNull
    @Column(name = "organizer_id")
    private Long organizerId;

    @NotNull
    @Column(name = "category_id")
    private Long categoryId;

    @NotNull
    @Column(name = "created_dt", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdDt;

    @Column(name = "updated_dt", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedDt;


}
