package com.pixelguardian.pharmanetapi.model.repository;

import com.pixelguardian.pharmanetapi.model.entity.EstoqueLote;
import com.pixelguardian.pharmanetapi.model.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EstoqueLoteRepository extends JpaRepository<EstoqueLote, Long> {

//    public Optional<EstoqueLote> findEstoqueLoteByProdutoOrderByDataValidade(LocalDate dataValidade);

    @Query("select e from EstoqueLote e where e.produto = ?1 and e.quantidade <> ?2")
    List<EstoqueLote> findByProdutoAndQuantidadeNot(Produto produto, Integer quantidade);


}
