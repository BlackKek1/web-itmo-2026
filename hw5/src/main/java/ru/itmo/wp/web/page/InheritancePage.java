package ru.itmo.wp.web.page;

import ru.itmo.wp.web.page.common.BasePage;

import java.util.Map;

@SuppressWarnings({"unused", "RedundantSuppression"})
public class InheritancePage extends BasePage {
    private void action(Map<String, Object> view) {
        view.put("message", "This action is declared in page class");
    }
}
