package com.example.demo;

import org.springframework.data.repository.CrudRepository;

public interface messageRepository extends CrudRepository<Messages, Long> {
}
