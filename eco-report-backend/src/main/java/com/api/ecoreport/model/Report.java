package com.api.ecoreport.model;

import com.api.ecoreport.model.enums.ReportStatus;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private String photoUrl;

    private String zipCode;

    @Enumerated(EnumType.STRING)
    private ReportStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Account account;

}
