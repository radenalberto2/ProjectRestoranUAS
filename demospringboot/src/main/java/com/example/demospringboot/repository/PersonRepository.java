package com.example.demospringboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demospringboot.entity.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
}