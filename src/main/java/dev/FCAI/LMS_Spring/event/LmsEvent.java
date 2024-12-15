package dev.FCAI.LMS_Spring.event;

import org.springframework.context.ApplicationEvent;

// Base Event Definitions
public class LmsEvent extends ApplicationEvent {
    private final String eventType;
    private final Object data;

    public LmsEvent(Object source, String eventType, Object data) {
        super(source);
        this.eventType = eventType;
        this.data = data;
    }

    public String getEventType() {
        return eventType;
    }

    public Object getData() {
        return data;
    }
}

