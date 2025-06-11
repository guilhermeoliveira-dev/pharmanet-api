package com.pixelguardian.pharmanetapi.api.dto;

import com.pixelguardian.pharmanetapi.model.entity.Categoria;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaDTO {

    private Long id;
    private String nome;
    private String descricao;

    private Long idCategoriaPai;
    private String nomeCategoriaPai;
    private String descricaoCategoriaPai;

    public static CategoriaDTO create(Categoria categoria) {
        ModelMapper modelMapper = new ModelMapper();
        CategoriaDTO dto = modelMapper.map(categoria, CategoriaDTO.class);

        if (categoria.getCategoriaPai() != null){
            dto.idCategoriaPai = categoria.getCategoriaPai().getId();
            dto.nomeCategoriaPai = categoria.getCategoriaPai().getNome();
            dto.descricaoCategoriaPai = categoria.getCategoriaPai().getDescricao();
        }

        return dto;
    }
}