package org.example.emailsend.service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.mail.MessagingException;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import org.example.emailsend.model.EmailFile;
import org.example.emailsend.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@CrossOrigin("*")
public class EmailService {

    @Autowired
    private FileRepository fileRepository;

    public EmailService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public List<String> uploadFile(MultipartFile file, String username, String password) throws IOException, MessagingException {
        Set<EmailFile> files = parseCsv(file);
        fileRepository.saveAll(files);
        for (EmailFile emailFile : files) {
            System.out.println(emailFile.getEmail());
            System.out.println(emailFile.getSubject());
            System.out.println(emailFile.getBody());
        }
        List<String> status = sendEmails(files, username, password);
        return status;
    }

    public Set<EmailFile> parseCsv(MultipartFile file) throws IOException {
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            HeaderColumnNameMappingStrategy<EmailFile> strategy = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(EmailFile.class);

            CsvToBean<EmailFile> csvToBean = new CsvToBeanBuilder<EmailFile>(reader)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withMappingStrategy(strategy)
                    .withIgnoreEmptyLine(true)
                    .build();
            return csvToBean.parse().stream().collect(Collectors.toSet());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private List<String> sendEmails(Set<EmailFile> files, String username, String password) throws MessagingException {

        List<String> statusList = new ArrayList<>();

        try {
            JavaMailSenderImpl mailSender = (JavaMailSenderImpl) javaMailSender(username, password);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(username);

            for (EmailFile emailFile : files) {
                String email = emailFile.getEmail();
                if (checkValid(email)) {
                    String subject = emailFile.getSubject();
                    String body = emailFile.getBody();
                    message.setTo(email);
                    message.setSubject(subject);
                    message.setText(body);
                    mailSender.send(message);
                    System.out.println("✅ Sent to: " + email);
                }
                else{
                    System.out.println("❌ Cannot Send to: " + email);
                }

            }
        } catch (Exception e) {
            statusList.add("❌ Could not initialize mail sender - " + e.getMessage());
        }
        return statusList;
    }


    public JavaMailSender javaMailSender(String username, String password) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }


    public boolean checkValid(String email) {

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) return false;
        String domain = email.substring(email.indexOf("@") + 1);
        if (domain.startsWith("example")) return false;
        try {
            Hashtable<String, String> env = new Hashtable<>();
            env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
            DirContext ctx = new InitialDirContext(env);
            Attributes attrs = ctx.getAttributes(domain, new String[]{"MX"});
            return attrs != null && attrs.get("MX") != null;
        } catch (NamingException e) {
            return false;
        }
    }
}

