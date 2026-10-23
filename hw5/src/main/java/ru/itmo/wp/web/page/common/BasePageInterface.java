package ru.itmo.wp.web.page.common;

import java.util.Map;

public interface BasePageInterface {
    default void baseActionFromInterface(Map<String, Object> view) {
        view.put("message", "This action is a default method of interface BasePageInterface");
    }
}
