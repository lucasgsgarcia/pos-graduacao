package br.edu.utfpr.exemplo.controller.requests;

import java.time.LocalDateTime;

public class ParkVehicleRequest{
    public long parkingId;
    public long vehicleId;
    public LocalDateTime startDate;
    public LocalDateTime endDate;
}
