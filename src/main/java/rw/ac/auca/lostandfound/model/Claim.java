package rw.ac.auca.lostandfound.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "claims")
@Data
@NoArgsConstructor
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @NotBlank(message = "Claimant name is required")
    @Column(nullable = false)
    private String claimantName;

    @Temporal(TemporalType.DATE)
    private Date claimDate;

    @NotBlank(message = "Status is required")
    @Column(nullable = false)
    private String status;
}