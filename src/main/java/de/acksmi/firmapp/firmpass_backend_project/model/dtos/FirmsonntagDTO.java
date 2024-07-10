package de.acksmi.firmapp.firmpass_backend_project.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FirmsonntagDTO {
    private Long id;
    private String name;
    private String content;
    private boolean completed;
}

