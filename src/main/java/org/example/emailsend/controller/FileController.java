package org.example.emailsend.controller;

import jakarta.mail.MessagingException;
import kong.unirest.GetRequest;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import org.example.emailsend.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

@RestController
@CrossOrigin("*")
public class FileController {

    @Autowired
    private EmailService emailService;


    public FileController(EmailService emailService) {
        this.emailService = emailService;
    }



    @GetMapping("checkvalid")
    public boolean checkValid(@RequestParam("email") String email) {
        return emailService.checkValid(email);
    }

    @PostMapping(value = "uploadfile",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<String>> uploadFile(@RequestParam("file") MultipartFile file, String username, String password) throws IOException, MessagingException {
        List<String> emailStatus= emailService.uploadFile(file,username, password);
        return ResponseEntity.ok(emailStatus);
    }

}
