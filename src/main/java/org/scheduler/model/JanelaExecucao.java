package org.scheduler.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.scheduler.controller.DateTimeDeserializer;

import java.time.LocalDateTime;

public class JanelaExecucao {

    private LocalDateTime inicio;

    private LocalDateTime fim;

    public JanelaExecucao() {

    }

    public JanelaExecucao(LocalDateTime inicio, LocalDateTime fim) {
        this.inicio = inicio;
        this.fim = fim;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public LocalDateTime getFim() {
        return fim;
    }

    @JsonDeserialize(using = DateTimeDeserializer.class)
    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }

    @JsonDeserialize(using = DateTimeDeserializer.class)
    public void setFim(LocalDateTime fim) {
        this.fim = fim;
    }

}
