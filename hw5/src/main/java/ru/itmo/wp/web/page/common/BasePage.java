package ru.itmo.wp.web.page.common;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public abstract class BasePage implements BasePageInterface {
    protected void baseAction(Map<String, Object> view, HttpServletRequest request) {
        view.put("message", "This action is a method of abstract class BasePage. " +
            "Uri: " + request.getRequestURI());
    }
}
