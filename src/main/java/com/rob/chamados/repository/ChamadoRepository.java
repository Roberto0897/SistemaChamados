package com.rob.chamados.repository;

import com.rob.chamados.entity.Chamado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChamadoRepository extends JpaRepository<Chamado, Long> {
    List<Chamado> findByUsuarioId(Long usuarioId);
}