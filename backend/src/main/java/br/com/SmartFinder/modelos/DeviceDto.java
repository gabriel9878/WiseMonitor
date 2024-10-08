package br.com.SmartFinder.modelos;

import jakarta.validation.constraints.NotEmpty;

public record DeviceDto(

    Long id,
    @NotEmpty
    String nome,
    Long user_id

) {}
