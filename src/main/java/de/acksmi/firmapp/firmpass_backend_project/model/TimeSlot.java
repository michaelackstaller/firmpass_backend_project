package de.acksmi.firmapp.firmpass_backend_project.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TimeSlot {
    private String day;
    private String time;


    public TimeSlot(String day, String time) {
        this.day = day;
        this.time = time;
    }
}