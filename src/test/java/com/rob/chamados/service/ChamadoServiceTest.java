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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChamadoServiceTest {

    @Mock
    private ChamadoRepository chamadoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ChamadoService chamadoService;

    private Usuario usuarioComum;
    private Usuario usuarioAdmin;
    private Categoria categoria;

    @BeforeEach
    void setUp() {
        usuarioComum = new Usuario(1L, "Roberto", "roberto@teste.com", "senhaHash", Role.USER, null);
        usuarioAdmin = new Usuario(2L, "Admin", "admin@teste.com", "senhaHash", Role.ADMIN, null);
        categoria = new Categoria(1L, "Hardware", "Problemas físicos", null);
    }

    @Test
    void usuarioComumDeveVerApenasOsProprosChamados() {
        when(usuarioRepository.findByEmail("roberto@teste.com"))
                .thenReturn(Optional.of(usuarioComum));

        Chamado chamado = new Chamado();
        chamado.setId(1L);
        chamado.setTitulo("Impressora não liga");
        chamado.setStatus(Status.ABERTO);
        chamado.setPrioridade(Prioridade.ALTA);
        chamado.setUsuario(usuarioComum);
        chamado.setCategoria(categoria);

        when(chamadoRepository.findByUsuarioId(1L))
                .thenReturn(List.of(chamado));

        List<ChamadoResponseDTO> resultado = chamadoService.listar("roberto@teste.com");

        assertEquals(1, resultado.size());
        assertEquals("Impressora não liga", resultado.get(0).getTitulo());
        verify(chamadoRepository, never()).findAll();
    }

    @Test
    void adminDeveVerTodosOsChamados() {
        when(usuarioRepository.findByEmail("admin@teste.com"))
                .thenReturn(Optional.of(usuarioAdmin));

        Chamado chamado1 = new Chamado();
        chamado1.setId(1L);
        chamado1.setTitulo("Chamado do usuário 1");
        chamado1.setStatus(Status.ABERTO);
        chamado1.setPrioridade(Prioridade.BAIXA);
        chamado1.setUsuario(usuarioComum);
        chamado1.setCategoria(categoria);

        Chamado chamado2 = new Chamado();
        chamado2.setId(2L);
        chamado2.setTitulo("Chamado do admin");
        chamado2.setStatus(Status.ABERTO);
        chamado2.setPrioridade(Prioridade.MEDIA);
        chamado2.setUsuario(usuarioAdmin);
        chamado2.setCategoria(categoria);

        when(chamadoRepository.findAll())
                .thenReturn(List.of(chamado1, chamado2));

        List<ChamadoResponseDTO> resultado = chamadoService.listar("admin@teste.com");

        assertEquals(2, resultado.size());
        verify(chamadoRepository, never()).findByUsuarioId(anyLong());
    }

    @Test
    void deveCriarChamadoVinculadoAoUsuarioLogado() {
        when(usuarioRepository.findByEmail("roberto@teste.com"))
                .thenReturn(Optional.of(usuarioComum));
        when(categoriaRepository.findById(1L))
                .thenReturn(Optional.of(categoria));

        ChamadoRequestDTO dto = new ChamadoRequestDTO();
        dto.setTitulo("Monitor piscando");
        dto.setDescricao("Tela pisca a cada 5 segundos");
        dto.setPrioridade("MEDIA");
        dto.setCategoriaId(1L);

        ChamadoResponseDTO resultado = chamadoService.criar(dto, "roberto@teste.com");

        assertEquals("Monitor piscando", resultado.getTitulo());
        assertEquals("Roberto", resultado.getSolicitanteNome());
        verify(chamadoRepository, times(1)).save(any(Chamado.class));
    }
}