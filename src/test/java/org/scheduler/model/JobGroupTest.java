package org.scheduler.model;

import org.junit.Before;
import org.junit.Test;
import org.scheduler.logic.Scheduler;

import static java.time.LocalDateTime.of;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JobGroupTest {

    @Test
    public void testInclusaoJobEntreJobsFuncionaComoEsperado() {
        JanelaExecucao janelaExecucao = new JanelaExecucao(of(2019, 11, 10, 9, 0, 0), of(2019, 11, 11, 12, 0, 0));
        long duracaoMaximaGrupo = 72L;

        JobGroup jobGroup = new JobGroup(janelaExecucao, duracaoMaximaGrupo);

        Job job1 = Job
                .builder()
                .id(1)
                .descricao("Importação de arquivos de fundos")
                .dataMaximaConclusao(of(2019, 11, 10, 12, 0, 0))
                .tempoEstimado(2L)
                .build();

        Job job2 = Job
                .builder()
                .id(2)
                .descricao("Importação de arquivos de fundos")
                .dataMaximaConclusao(of(2019, 11, 11, 12, 0, 0))
                .tempoEstimado(4L)
                .build();

        Job job3 = Job
                .builder()
                .id(3)
                .descricao("Importação de arquivos de fundos")
                .dataMaximaConclusao(of(2019, 11, 11, 8, 0, 0))
                .tempoEstimado(6L)
                .build();


        boolean incluiu1 = jobGroup.scheduleJob(job1);
        boolean incluiu2 = jobGroup.scheduleJob(job2);
        boolean incluiu3 = jobGroup.scheduleJob(job3);

        assertTrue(incluiu1);
        assertTrue(incluiu2);
        assertTrue(incluiu3);

        assertEquals(Integer.valueOf(1), jobGroup.getJobs().get(0).getId());
        assertEquals(Integer.valueOf(3), jobGroup.getJobs().get(1).getId());
        assertEquals(Integer.valueOf(2), jobGroup.getJobs().get(2).getId());
    }

}