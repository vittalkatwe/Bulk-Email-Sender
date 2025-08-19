package org.example.emailsend.repository;

import org.example.emailsend.model.EmailFile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends MongoRepository<EmailFile,Integer> {

}
