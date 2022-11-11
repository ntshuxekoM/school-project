package com.phishing.app.mail;


public class Mail {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String from;
    private String[] to;
    private String[] cc;
    private String subject;
    private String content;

    public Mail() {
        content = "text/plain";
    }


    public Mail(Integer id, String from, String[] to, String[] cc, String subject, String content) {
        super();
        this.id = id;
        this.from = from;
        this.to = to;
        this.cc = cc;
        this.subject = subject;
        this.content = content;
    }


    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    @Override
    public String toString() {
        return "Mail{" +
            "from='" + from + '\'' +
            ", to='" + to + '\'' +
            ", subject='" + subject + '\'' +
            ", content='" + content + '\'' +
            '}';
    }

    public String[] getTo() {
        return to;
    }

    public void setTo(String[] to) {
        this.to = to;
    }

    public String[] getCc() {
        return cc;
    }

    public void setCc(String[] cc) {
        this.cc = cc;
    }

}
