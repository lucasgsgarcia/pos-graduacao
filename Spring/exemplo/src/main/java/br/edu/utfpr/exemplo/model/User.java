package br.edu.utfpr.exemplo.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "DT_USER")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String Name;

    private String Email;

    private String Password;

    private String PhoneNumber;

    private String Document;
}
