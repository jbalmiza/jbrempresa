package com.jbrempresa.backend.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import com.jbrempresa.backend.entity.HorarioRecurso;

@DataJpaTest
class HorarioRecursoRepositoryTest {
    @Autowired private HorarioRecursoRepository repository;

    @Test
    void sustituyeSinColisionElMismoHorarioDeJuevesADomingo() {
        guardarHorarioSolicitado();

        repository.deleteByEmpIdAndRagId(1L, 9L);
        repository.flush();
        guardarHorarioSolicitado();

        var resultado = repository.findByEmpIdAndRagIdAndHorActTrueOrderByHorDiaAscHorIniAsc(1L, 9L);
        assertThat(resultado).hasSize(4);
        assertThat(resultado).extracting(HorarioRecurso::getHorDia).containsExactly(4, 5, 6, 7);
        assertThat(resultado).allSatisfy(horario -> {
            assertThat(horario.getHorIni()).isEqualTo(LocalTime.of(19, 30));
            assertThat(horario.getHorFin()).isEqualTo(LocalTime.of(23, 30));
        });
    }

    private void guardarHorarioSolicitado() {
        for (int dia = 4; dia <= 7; dia++) {
            HorarioRecurso horario = new HorarioRecurso();
            horario.setEmpId(1L);
            horario.setRagId(9L);
            horario.setHorDia(dia);
            horario.setHorIni(LocalTime.of(19, 30));
            horario.setHorFin(LocalTime.of(23, 30));
            horario.setHorUsuMov("prueba");
            horario.setHorFecMov(LocalDateTime.of(2026, 9, 7, 19, 0));
            horario.setHorAct(true);
            repository.save(horario);
        }
        repository.flush();
    }
}
