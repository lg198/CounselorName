package com.github.lg198.cnotes.bean;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Note {

    private String title;
    private String contents;
    private String studentId;
    private String id;
    private ZonedDateTime creationDate;


    public Note(String t, String i, String si, String c) {
        this(t, i, si, ZonedDateTime.now(ZoneOffset.of("+0")), c);
    }

    public Note(String t, String i, String si, String cd, String c) {
        this(t, i, si, ZonedDateTime.from(DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(cd)), c);
    }

    public Note(String t, String i, String si, ZonedDateTime cd, String c) {
        title = t;
        id = i;
        studentId = si;
        creationDate = cd;
        contents = c;
    }

    public String getDateString() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        return formatter.format(creationDate);
    }

    public String getId() {
        return id;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getContents() {
        return contents;
    }

    public String getTitle() {
        return title;
    }
}
