package de.acksmi.firmapp.firmpass_backend_project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "firmstunde")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Firmstunde {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String content;
}
