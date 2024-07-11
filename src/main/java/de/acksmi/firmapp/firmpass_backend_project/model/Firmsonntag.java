package de.acksmi.firmapp.firmpass_backend_project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "firmsonntag")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Firmsonntag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String content;
    private LocalDate date; //Date an dem der Firmsonntag stattfindet
}
