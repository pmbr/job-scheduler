package org.scheduler.logic;

import org.junit.Test;
import org.scheduler.model.JanelaExecucao;
import org.scheduler.model.Job;

import static java.time.LocalDateTime.of;
import static org.junit.Assert.assertEquals;

public class SchedulerTest {

    private Scheduler scheduler = new Scheduler();

    @Test
    public void testEmptyJobsReturnsEmptySchedules() {
        JanelaExecucao janelaExecucao = new JanelaExecucao(of(2019, 11, 10, 9, 0, 0), of(2019, 11, 11, 12, 0, 0));

        Job[] jobs = new Job[] {};
        Integer[][] schedules = scheduler.createJobGroups(janelaExecucao, jobs);
        assertEquals(0, schedules.length);
    }

    @Test
    public void testUmJob() {
        JanelaExecucao janelaExecucao = new JanelaExecucao(of(2019, 11, 10, 9, 0, 0), of(2019, 11, 11, 12, 0, 0));

        Job job1 = Job
                .builder()
                .id(1)
                .descricao("Importação de arquivos de fundos")
                .dataMaximaConclusao(of(2019, 11, 10, 12, 0, 0))
                .tempoEstimado(2L)
                .build();

        Job[] jobs = new Job[] { job1 };

        Integer[][] jobGroups = scheduler.createJobGroups(janelaExecucao, jobs);
        assertEquals(1, jobGroups.length);

        Integer[] grupo1 = jobGroups[0];
        assertEquals(1, grupo1.length);
        assertEquals(1, (int)grupo1[0]);
    }

    @Test
    public void testMassaDadosExemplo() {
        JanelaExecucao janelaExecucao = new JanelaExecucao(of(2019, 11, 10, 9, 0, 0), of(2019, 11, 11, 12, 0, 0));

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

        Job[] jobs = new Job[] { job1, job2, job3 };

        Integer[][] jobGroups = scheduler.createJobGroups(janelaExecucao, jobs);
        assertEquals(2, jobGroups.length);

        Integer[] grupo1 = jobGroups[0];
        assertEquals(2, grupo1.length);
        assertEquals(1, (int)grupo1[0]);
        assertEquals(3, (int)grupo1[1]);

        Integer[] grupo2 = jobGroups[1];
        assertEquals(1, grupo2.length);
        assertEquals(2, (int)grupo2[0]);
    }

}