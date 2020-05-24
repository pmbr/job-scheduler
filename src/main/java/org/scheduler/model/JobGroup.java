package org.scheduler.model;

import org.apache.tomcat.jni.Local;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.LinkedList;

import static java.time.Duration.between;
import static java.time.Duration.ofHours;
import static java.time.temporal.ChronoUnit.HOURS;
import static java.time.temporal.ChronoUnit.SECONDS;

public class JobGroup {

    private final long SECONDS_PER_HOUR = 3600L;

    private final long duracaoMaximaGrupo;

    private final JanelaExecucao janelaExecucao;
    private final long duracaoJanelaExecucao;

    private LinkedList<Job> jobs = new LinkedList<>();
    private long tempoTotalEstimado = 0;

    public JobGroup(JanelaExecucao janelaExecucao, long duracaoMaximaGrupo) {
        this.duracaoMaximaGrupo = duracaoMaximaGrupo;
        this.janelaExecucao = janelaExecucao;
        this.duracaoJanelaExecucao = between(janelaExecucao.getInicio(), janelaExecucao.getFim()).get(SECONDS) / SECONDS_PER_HOUR;
    }

    public JobGroup(JanelaExecucao janelaExecucao, long duracaoMaximaGrupo, Job primeiroJob) {
        this(janelaExecucao, duracaoMaximaGrupo);
        jobs.add(primeiroJob);
        this.tempoTotalEstimado = primeiroJob.getTempoEstimado();
    }

    public LinkedList<Job> getJobs() {
        return jobs;
    }

    public boolean scheduleJob(Job novoJob) {
        if (this.jobs.isEmpty()) {
            jobs.add(novoJob);
            this.tempoTotalEstimado = novoJob.getTempoEstimado();
            return true;
        } else if (novoJob.antes(jobs.getFirst())) {
            if (inclusaoRespeitaDuracaoMaximaGrupo(novoJob)
                    && inclusaoRespeitaJanelaExecucao(novoJob)
                    && inclusaoNoInicioRespeitaDataMaximaConclusao(novoJob)) {
                jobs.addFirst(novoJob);
                this.tempoTotalEstimado = this.tempoTotalEstimado + novoJob.getTempoEstimado();
                return true;
            }
        } else if (novoJob.depois(jobs.getLast())) {
            if (inclusaoRespeitaDuracaoMaximaGrupo(novoJob)
                    && inclusaoRespeitaJanelaExecucao(novoJob)
                    && inclusaoNoFinalRespeitaDataMaximaConclusao(novoJob)) {
                jobs.addLast(novoJob);
                this.tempoTotalEstimado = this.tempoTotalEstimado + novoJob.getTempoEstimado();
                return true;
            }
        } else {
            int indexJobAnterior = encontraJobAnterior(novoJob);
            if (inclusaoRespeitaDuracaoMaximaGrupo(novoJob)
                    && inclusaoRespeitaJanelaExecucao(novoJob)
                    && inclusaoAposRespeitaDataMaximaConclusao(novoJob, indexJobAnterior)) {
                jobs.add(indexJobAnterior + 1, novoJob);
                this.tempoTotalEstimado = this.tempoTotalEstimado + novoJob.getTempoEstimado();
                return true;
            }
        }
        return false;
    }

    private boolean inclusaoRespeitaDuracaoMaximaGrupo(Job newJob) {
        return this.tempoTotalEstimado + newJob.getTempoEstimado() <= this.duracaoMaximaGrupo;
    }

    private boolean inclusaoRespeitaJanelaExecucao(Job newJob) {
        return this.tempoTotalEstimado + newJob.getTempoEstimado() <= this.duracaoJanelaExecucao;
    }

    private boolean inclusaoNoInicioRespeitaDataMaximaConclusao(Job novoJob) {
        LocalDateTime horaEstimadaFimJobAnterior = this.janelaExecucao.getInicio()
                .plus(ofHours(novoJob.getTempoEstimado()));

        for (Job job: this.jobs) {
            LocalDateTime horaEstimadaFimJob = horaEstimadaFimJobAnterior.plus(ofHours(job.getTempoEstimado()));
            if (horaEstimadaFimJob.isAfter(job.getDataMaximaConclusao())) {
                return false;
            }
            horaEstimadaFimJobAnterior = horaEstimadaFimJob;
        }
        return true;
    }

    private boolean inclusaoNoFinalRespeitaDataMaximaConclusao(Job novoJob) {
        LocalDateTime horaEstimadaFimNovoJob = this.janelaExecucao.getInicio()
                                                .plus(ofHours(this.tempoTotalEstimado))
                                                .plus(ofHours(novoJob.getTempoEstimado()));

        return horaEstimadaFimNovoJob.equals(novoJob.getDataMaximaConclusao()) ||
                horaEstimadaFimNovoJob.isBefore(novoJob.getDataMaximaConclusao());
    }

    private boolean inclusaoAposRespeitaDataMaximaConclusao(Job novoJob, int indexJobAnterior) {
        long totalTempoEstimadoJobsAnteriores = 0;
        for (int i = 0; i <= indexJobAnterior; i++) {
            totalTempoEstimadoJobsAnteriores = totalTempoEstimadoJobsAnteriores + this.jobs.get(i).getTempoEstimado();
        }

        //Job jobAnterior = this.getJobs().get(indexJobAnterior);

        LocalDateTime horaEstimadaFimNovoJob = this.janelaExecucao.getInicio()
                .plus(ofHours(totalTempoEstimadoJobsAnteriores))
                .plus(ofHours(novoJob.getTempoEstimado()));

        if (horaEstimadaFimNovoJob.isAfter(novoJob.getDataMaximaConclusao())) return false;

        LocalDateTime horaEstimadaFimJobAnterior = horaEstimadaFimNovoJob;

        for (int i = indexJobAnterior + 1; i < this.jobs.size(); i++) {
            Job job = this.jobs.get(i);
            LocalDateTime horaEstimadaFimJob = horaEstimadaFimJobAnterior.plus(ofHours(job.getTempoEstimado()));
            if (horaEstimadaFimJob.isAfter(job.getDataMaximaConclusao())) {
                return false;
            }
            horaEstimadaFimJobAnterior = horaEstimadaFimJob;
        }
        return true;
    }

    private int encontraJobAnterior(Job novoJob) {
        for (int i = 0; i < jobs.size(); i++) {
            Job job = this.jobs.get(i);
            if (job.getDataMaximaConclusao().equals(novoJob.getDataMaximaConclusao())
                    || job.getDataMaximaConclusao().isBefore(novoJob.getDataMaximaConclusao())) {
                return i;
            }
        }
        return -1;
    }

    public Integer[] jobIds() {
        Integer[] ids = new Integer[jobs.size()];
        for (int i = 0; i < jobs.size(); i++) {
            ids[i] = jobs.get(i).getId();
        }
        return ids;
    }

}
