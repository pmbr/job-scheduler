package org.scheduler.controller;

import org.scheduler.model.JanelaExecucao;
import org.scheduler.model.Job;

public class JobRequest {

    private JanelaExecucao janelaExecucao;
    private Job[] jobs;

    public JanelaExecucao getJanelaExecucao() {
        return janelaExecucao;
    }

    public void setJanelaExecucao(JanelaExecucao janelaExecucao) {
        this.janelaExecucao = janelaExecucao;
    }

    public Job[] getJobs() {
        return jobs;
    }

    public void setJobs(Job[] jobs) {
        this.jobs = jobs;
    }
}
