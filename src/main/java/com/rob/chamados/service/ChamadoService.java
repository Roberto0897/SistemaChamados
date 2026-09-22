package com.rob.chamados.service;

import com.rob.chamados.dto.ChamadoRequestDTO;
import com.rob.chamados.dto.ChamadoResponseDTO;
import com.rob.chamados.entity.Categoria;
import com.rob.chamados.entity.Chamado;
import com.rob.chamados.entity.Usuario;
import com.rob.chamados.enums.Prioridade;
import com.rob.chamados.enums.Role;
import com.rob.chamados.enums.Status;
import com.rob.chamados.repository.CategoriaRepository;
import com.rob.chamados.repository.ChamadoRepository;
import com.rob.chamados.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChamadoService {

    private final ChamadoRepository chamadoRepository;
    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;

    public ChamadoResponseDTO criar(ChamadoRequestDTO dto, String emailUsuarioLogado) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuarioLogado)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        Chamado chamado = new Chamado();
        chamado.setTitulo(dto.getTitulo());
        chamado.setDescricao(dto.getDescricao());
        chamado.setPrioridade(Prioridade.valueOf(dto.getPrioridade().toUpperCase()));
        chamado.setStatus(Status.ABERTO);
        chamado.setDataCriacao(LocalDateTime.now());
        chamado.setUsuario(usuario);
        chamado.setCategoria(categoria);

        chamadoRepository.save(chamado);

        return paraDTO(chamado);
    }

    public List<ChamadoResponseDTO> listar(String emailUsuarioLogado) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuarioLogado)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        List<Chamado> chamados;

        if (usuario.getRole() == Role.ADMIN) {
            chamados = chamadoRepository.findAll();
        } else {
            chamados = chamadoRepository.findByUsuarioId(usuario.getId());
        }

        return chamados.stream().map(this::paraDTO).toList();
    }

    public ChamadoResponseDTO mudarStatus(Long chamadoId, Status novoStatus) {
        Chamado chamado = chamadoRepository.findById(chamadoId)
                .orElseThrow(() -> new RuntimeException("Chamado não encontrado"));

        chamado.setStatus(novoStatus);

        if (novoStatus == Status.FECHADO) {
            chamado.setDataFechamento(LocalDateTime.now());
        }

        chamadoRepository.save(chamado);
        return paraDTO(chamado);
    }

    private ChamadoResponseDTO paraDTO(Chamado chamado) {
        return new ChamadoResponseDTO(
                chamado.getId(),
                chamado.getTitulo(),
                chamado.getDescricao(),
                chamado.getStatus(),
                chamado.getPrioridade(),
                chamado.getDataCriacao(),
                chamado.getCategoria().getNome(),
                chamado.getUsuario().getNome()
        );
    }
}