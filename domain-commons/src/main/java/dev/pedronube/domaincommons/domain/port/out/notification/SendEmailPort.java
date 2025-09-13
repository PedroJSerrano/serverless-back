package dev.pedronube.domaincommons.domain.port.out.notification;

import dev.pedronube.domaincommons.domain.model.notification.EmailRequest;

import java.util.function.Consumer;

public interface SendEmailPort extends Consumer<EmailRequest> {}
