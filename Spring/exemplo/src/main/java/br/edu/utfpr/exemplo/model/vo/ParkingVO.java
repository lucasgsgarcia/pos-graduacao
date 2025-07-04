package br.edu.utfpr.exemplo.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ParkingVO {
    private int id;
    private LocalDateTime UnavailableUntil;
}
