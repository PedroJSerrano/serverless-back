package dev.pedronube.domaincommons.domain.port.out.notification;

import java.util.function.BiConsumer;

public interface NotifySubscriberPort extends BiConsumer<String, String> {}
