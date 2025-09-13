package dev.pedronube.domaincommons.domain.model.notification;

public record EmailRequest(String to, String subject, String content) {}
