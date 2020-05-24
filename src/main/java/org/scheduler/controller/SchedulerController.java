package org.scheduler.controller;

import org.scheduler.exception.SchedulerException;
import org.scheduler.logic.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.scheduler.controller.ErrorResponse.of;

@RestController
public class SchedulerController {

    private final Scheduler scheduler;

    private Logger logger = LoggerFactory.getLogger(SchedulerController.class);

    public SchedulerController(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @RequestMapping(value = "/", method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> scheduleJobs(@RequestBody JobRequest jobRequest) {
        try {
            return ResponseEntity.ok(scheduler.createJobGroups(jobRequest.getJanelaExecucao(), jobRequest.getJobs()));
        } catch (SchedulerException se) {
            logger.error("Erro ao criar grupos de jobs", se);
            return ResponseEntity
                    .status(INTERNAL_SERVER_ERROR)
                    .body(of(se.getMessage()));

        }
    }

}
