package com.pixelguardian.pharmanetapi.api.dto;

import com.pixelguardian.pharmanetapi.model.entity.Pagamento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagamentoDTO {

    private Long id;
    private String dataPagamento;
    private Float valor;
    private String formaPagamento;
//    private NaoSei comprovante;

    public static PagamentoDTO create(Pagamento pagamento) {
        ModelMapper modelMapper = new ModelMapper();
        PagamentoDTO dto = modelMapper.map(pagamento, PagamentoDTO.class);
        return dto;
    }
}