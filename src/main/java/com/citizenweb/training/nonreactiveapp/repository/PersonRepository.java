package com.citizenweb.training.nonreactiveapp.repository;

import com.citizenweb.training.nonreactiveapp.model.Person;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface PersonRepository extends MongoRepository<Person, String> {
}
