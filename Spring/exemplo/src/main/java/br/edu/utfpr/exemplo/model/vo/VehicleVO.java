package br.edu.utfpr.exemplo.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class VehicleVO {
    @Schema(nullable = true)
    private long id;
    private String plate;
    private String model;
    private String color;
}
