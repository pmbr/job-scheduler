package org.scheduler.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.scheduler.controller.DateTimeDeserializer;

import java.time.LocalDateTime;

public class Job {

    private Integer id;
    private String descricao;
    private Long tempoEstimado;

    private LocalDateTime dataMaximaConclusao;

    public Job() {

    }

    public Job(Integer id, String descricao, LocalDateTime dataMaximaConclusao, Long tempoEstimado) {
        this.id = id;
        this.descricao = descricao;
        this.dataMaximaConclusao = dataMaximaConclusao;
        this.tempoEstimado = tempoEstimado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getDataMaximaConclusao() {
        return dataMaximaConclusao;
    }

    @JsonDeserialize(using = DateTimeDeserializer.class)
    public void setDataMaximaConclusao(LocalDateTime dataMaximaConclusao) {
        this.dataMaximaConclusao = dataMaximaConclusao;
    }

    public Long getTempoEstimado() {
        return tempoEstimado;
    }

    public void setTempoEstimado(Long tempoEstimado) {
        this.tempoEstimado = tempoEstimado;
    }

    public static JobBuilder builder() {
        return new JobBuilder();
    }

    public static class JobBuilder {

        Integer id;
        String descricao;
        LocalDateTime dataMaximaConclusao;
        Long tempoEstimado;

        private JobBuilder() {

        }

        public JobBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public JobBuilder descricao(String descricao) {
            this.descricao = descricao;
            return this;
        }

        public JobBuilder dataMaximaConclusao(LocalDateTime dataMaximaConclusao) {
            this.dataMaximaConclusao = dataMaximaConclusao;
            return this;
        }

        public JobBuilder tempoEstimado(Long tempoEstimado) {
            this.tempoEstimado = tempoEstimado;
            return this;
        }

        public Job build() {
            return new Job(id, descricao, dataMaximaConclusao, tempoEstimado);
        }
    }

    public boolean antes(Job otherJob) {
        return this.getDataMaximaConclusao().isBefore(otherJob.getDataMaximaConclusao());
    }

    public boolean depois(Job otherJob) {
        return this.getDataMaximaConclusao().isAfter(otherJob.getDataMaximaConclusao());
    }

}
