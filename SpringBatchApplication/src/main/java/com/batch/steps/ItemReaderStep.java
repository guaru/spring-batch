package com.batch.steps;

import com.batch.entities.Person;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.ResourceLoader;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@AllArgsConstructor
public class ItemReaderStep implements Tasklet {

    private final ResourceLoader resourceLoader;
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("------------------> Inicio del paso de lectura <------------------");
        Reader reader = new FileReader(resourceLoader.getResource("classpath:files/destinations/persons.csv").getFile());

        CSVParser parser =  new CSVParserBuilder()
                .withSeparator(',')
                .build();

        CSVReader csvReader =  new CSVReaderBuilder(reader).withCSVParser(parser)
                .withSkipLines(1).build();
        List<Person> personList =  new ArrayList<>();
        String[] currentLine;
        int positionName = 0;
        int postionLastName = 1;
        int positionAge = 2;
        while ((currentLine = csvReader.readNext()) != null) {
            Person person = new Person();
            person.setName(currentLine[positionName]);
            person.setLastName(currentLine[postionLastName]);
            person.setAge(Integer.parseInt(currentLine[positionAge]));
            personList.add(person);
        }
        csvReader.close();
        reader.close();
        log.info("------------------> Fin del paso de lectura <------------------");
        chunkContext.getStepContext()
                .getStepExecution()
                .getJobExecution()
                .getExecutionContext()
                .put("personList", personList);

        return RepeatStatus.FINISHED;
    }
}
