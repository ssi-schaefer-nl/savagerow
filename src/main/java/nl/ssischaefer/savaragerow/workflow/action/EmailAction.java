package nl.ssischaefer.savaragerow.workflow.action;

import java.util.Map;

public class EmailAction extends Action {
    private String email;
    private String subject;
    private String body;

    public String getEmail() {
        return email;
    }

    public EmailAction setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public EmailAction setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getBody() {
        return body;
    }

    public EmailAction setBody(String body) {
        this.body = body;
        return this;
    }

    @Override
    public void perform(Map<String, String> oldData) {
        System.out.println(String.format("Recipient: %s\nSubject: %s\nBody: %s", email, subject, body));
    }
}
