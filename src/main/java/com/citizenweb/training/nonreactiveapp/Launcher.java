package com.citizenweb.training.nonreactiveapp;

import com.citizenweb.training.nonreactiveapp.model.Person;
import com.citizenweb.training.nonreactiveapp.repository.PersonRepository;
import com.citizenweb.training.nonreactiveapp.service.PersonService;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Component
public class Launcher implements CommandLineRunner {

    private final PersonService personService;
    private final PersonRepository personRepository;

    public Launcher(PersonService personService, PersonRepository personRepository) {
        this.personService = personService;
        this.personRepository = personRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        long timeAtStart = System.currentTimeMillis();
        log.info("Launching [ ReactiveApp ]");

        int size = 100_000;
        char charToFind = 'O';

        List<String> personNameList = new ArrayList<>(size);
        List<Integer> personAgeList = new ArrayList<>(size);
        List<Person> savedPersonList = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            Person person = Person.builder()
                    .name(personService.buildName())
                    .age(personService.computeAge())
                    .build();
            Person savedPerson = recordPerson(person);
            log.debug("[ {} ] saved", savedPerson);
            savedPersonList.add(savedPerson);
        }

        List<Person> personListFromDatabase = personRepository.findAll();

        for (Person person : personListFromDatabase) {
            var dbEntries = personListFromDatabase.size();
            Assert.isTrue(size == dbEntries, () -> dbEntries + " Person objects found in DB");
            personNameList.add(person.getName());
            personAgeList.add(person.getAge());
        }

        double averageCharToFind = personService.computeMean(personNameList,charToFind);
        log.info("Average number of [ '{}' ] in names : [ {} ]", charToFind, averageCharToFind);
        var meanAge = personAgeList.stream().mapToInt(a -> a).average().orElse(0);
        log.info("Average age is [ {} ]", meanAge);

        log.info("[ {} ] Person objects were saved", savedPersonList.size());

        log.info("Elapsed time since start = [ {} ] ms", System.currentTimeMillis() - timeAtStart);
        System.exit(0);

    }

    private Person recordPerson(Person person) {
        Person savedPersonMono = personRepository.save(person);
        log.debug("Saving [ {} ]", person.getName());
        return savedPersonMono;
    }
}
