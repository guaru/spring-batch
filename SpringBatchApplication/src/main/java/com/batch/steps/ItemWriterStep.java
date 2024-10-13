package com.batch.steps;

import com.batch.entities.Person;
import com.batch.service.IPersonService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.List;

@Slf4j
@AllArgsConstructor
public class ItemWriterStep implements Tasklet {

    private final IPersonService personService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("------------------> Inicio del paso de escritura <------------------");
        List<Person> persons = (List<Person>) chunkContext.getStepContext()
                .getStepExecution()
                .getJobExecution()
                .getExecutionContext()
                .get("personList");
        persons.forEach(person -> {
            if(person != null){
                log.info("Persona: {}", person);
            }
        });
        personService.saveAll(persons);
        log.info("------------------> Fin del paso de escritura <------------------");
        return RepeatStatus.FINISHED;
    }
}
