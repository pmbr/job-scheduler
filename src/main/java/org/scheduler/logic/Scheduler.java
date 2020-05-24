package org.scheduler.logic;

import org.scheduler.exception.SchedulerException;
import org.scheduler.model.JanelaExecucao;
import org.scheduler.model.Job;
import org.scheduler.model.JobGroup;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Component
public class Scheduler {

    private final long DURACAO_MAXIMA_GRUPO = 8L;

    public Integer[][] createJobGroups(JanelaExecucao janelaExecucao, Job[] jobs) throws SchedulerException {
        List<JobGroup> jobGroups = new ArrayList<>();

        for (Job job: sortByMaximaConclusao(jobs)) {
            boolean criaNovoGrupo = true;
            if (!jobGroups.isEmpty()) {
                for (JobGroup jobGroup: jobGroups) {
                    if (jobGroup.scheduleJob(job)) {
                        criaNovoGrupo = false;
                        break;
                    }
                }
            }
            if (criaNovoGrupo) {
                jobGroups.add(new JobGroup(janelaExecucao, DURACAO_MAXIMA_GRUPO, job));
            }
        }
        return convert(jobGroups);
    }

    private Job[] sortByMaximaConclusao(Job[] jobs) {
        Arrays.sort(jobs, Comparator.comparing(Job::getDataMaximaConclusao));
        return jobs;
    }

    private Integer[][] convert(List<JobGroup> jobGroups) {
        Integer[][] jobGroupIds = new Integer[jobGroups.size()][];
        for (int i = 0; i < jobGroups.size(); i++) {
            jobGroupIds[i] = jobGroups.get(i).jobIds();
        }
        return jobGroupIds;
    }

}
