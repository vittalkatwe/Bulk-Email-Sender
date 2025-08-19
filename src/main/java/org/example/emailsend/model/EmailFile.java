package org.example.emailsend.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "emailfile")
@Getter
@Data
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailFile {
    @Id
    private String id;
    private String email;
    private String subject;
    private String body;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
