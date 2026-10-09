package ru.itmo.wp.servlet;

import ru.itmo.wp.util.ImageUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Base64;
import java.util.concurrent.ThreadLocalRandom;

public class CaptchaFilter implements Filter {
    private static final String PASSED_SESSION_ATTRIBUTE = "captchaPassed";

    private static final String ANSWER_PARAMETER = "captchaAnswer";
    private static final String EXPECTED_ANSWER_ATTRIBUTE = "captchaExpected";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
        throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();

        if (!"GET".equals(request.getMethod()) || Boolean.TRUE.equals(session.getAttribute(PASSED_SESSION_ATTRIBUTE))) {
            chain.doFilter(request, response);
            return;
        }

        String answer = request.getParameter(ANSWER_PARAMETER);
        if (answer != null) {
            if (answer.equals(session.getAttribute(EXPECTED_ANSWER_ATTRIBUTE))) {
                session.setAttribute(PASSED_SESSION_ATTRIBUTE, true);
                session.removeAttribute(EXPECTED_ANSWER_ATTRIBUTE);
                response.sendRedirect(request.getRequestURI());
                return;
            }
            session.setAttribute(EXPECTED_ANSWER_ATTRIBUTE, generateExpected());
        }

        writeForm(response, getOrCreateExpected(session));
    }

    private String getOrCreateExpected(HttpSession session) {
        String expected = (String) session.getAttribute(EXPECTED_ANSWER_ATTRIBUTE);
        if (expected == null) {
            expected = generateExpected();
            session.setAttribute(EXPECTED_ANSWER_ATTRIBUTE, expected);
        }
        return expected;
    }

    private String generateExpected() {
        return String.valueOf(ThreadLocalRandom.current().nextInt(100, 1000));
    }

    private void writeForm(HttpServletResponse response, String expected) throws IOException {
        String image = Base64.getEncoder().encodeToString(ImageUtils.toPng(expected));
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-store");
        response.getWriter().print(
            "<!DOCTYPE html><html><head><title>Captcha</title></head><body>"
                + "<img src=\"data:image/png;base64," + image + "\" alt=\"captcha\">"
                + "<form method=\"get\">"
                + "<input name=\"" + ANSWER_PARAMETER + "\" autofocus autocomplete=\"off\">"
                + "<button type=\"submit\">Submit</button>"
                + "</form></body></html>"
        );
        response.getWriter().flush();
    }
}