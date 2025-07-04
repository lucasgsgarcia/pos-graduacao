package br.edu.utfpr.exemplo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table
@Data
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String plate;

    private String model;

    private String color;

    @ManyToMany
    @JoinTable(
            name = "vehicle_parking",
            joinColumns = @JoinColumn(name = "vehicle_id"),
            inverseJoinColumns = @JoinColumn(name = "parking_id")
    )
    private List<Parking> parkings;
}
