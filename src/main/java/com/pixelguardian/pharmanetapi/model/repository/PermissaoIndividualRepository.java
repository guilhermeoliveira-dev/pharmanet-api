package com.pixelguardian.pharmanetapi.model.repository;

import com.pixelguardian.pharmanetapi.model.entity.Cargo;
import com.pixelguardian.pharmanetapi.model.entity.Permissao;
import com.pixelguardian.pharmanetapi.model.entity.PermissaoIndividual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PermissaoIndividualRepository extends JpaRepository<PermissaoIndividual, Long> {

    List<PermissaoIndividual> findByCargo(Cargo cargo);

    @Query("select p from PermissaoIndividual p where p.cargo = ?1 and p.permissao = ?2")
    Optional<PermissaoIndividual> findByCargoAndPermissao(Cargo cargo, Permissao permissao);

    @Transactional
    @Modifying
    @Query("delete from PermissaoIndividual p where p.cargo = ?1")
    int deleteByCargo(Cargo cargo);






}
