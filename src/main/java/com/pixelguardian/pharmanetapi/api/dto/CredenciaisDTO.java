package com.pixelguardian.pharmanetapi.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CredenciaisDTO {
    private String email;
    private String senha;
}