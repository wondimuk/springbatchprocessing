package com.wk.batchprocess.processor;

import com.wk.batchprocess.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.item.ItemProcessor;

public class PersonItemProcessor implements ItemProcessor<Employee, Employee> {

    private static final Logger log = LoggerFactory.getLogger(PersonItemProcessor.class);

    @Override
    public Employee process(final Employee person) {
        final String firstName = person.getFirstName().toUpperCase();
        final String lastName = person.getLastName().toUpperCase();
        final String address = (person.getAddress().contains("usa")==true?person.getAddress().toUpperCase():person.getAddress().substring(0,1).toUpperCase()+person.getAddress().substring(1).toLowerCase());

        final Employee transformedPerson = new Employee(firstName, lastName, address);

        log.info("Converting (" + person + ") into (" + transformedPerson + ")");

        return transformedPerson;
    }

}
