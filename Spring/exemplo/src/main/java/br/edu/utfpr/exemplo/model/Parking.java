package br.edu.utfpr.exemplo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table
@Data
public class Parking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDateTime unavailableUntil;

    @ManyToMany(mappedBy = "parkings")
    private List<Vehicle> vehicles;
}
