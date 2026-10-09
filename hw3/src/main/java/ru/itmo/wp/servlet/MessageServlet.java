package ru.itmo.wp.servlet;

import com.google.gson.Gson;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MessageServlet extends HttpServlet {
    private static final String USER_NAME_PARAMETER = "user";

    private final List<Message> messages = new CopyOnWriteArrayList<>();
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        String path = request.getPathInfo();
        if ("/auth".equals(path)) {
            auth(request, response);
        } else if ("/findAll".equals(path)) {
            findAll(response);
        } else if ("/add".equals(path)) {
            add(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void auth(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        String user = request.getParameter(USER_NAME_PARAMETER);
        if (user != null && !user.trim().isEmpty()) {
            session.setAttribute(USER_NAME_PARAMETER, user);
        }

        String currentUser = (String) session.getAttribute(USER_NAME_PARAMETER);
        writeAsJson(response, currentUser == null ? "" : currentUser);
    }

    private void findAll(HttpServletResponse response) throws IOException {
        writeAsJson(response, messages);
    }

    private void add(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String user = (String) request.getSession().getAttribute(USER_NAME_PARAMETER);
        String text = request.getParameter("text");

        if (user == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        } else if (text == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            messages.add(new Message(user, text));
            writeAsJson(response, "");
        }
    }

    private void writeAsJson(HttpServletResponse response, Object object) throws IOException {
        response.getWriter().print(gson.toJson(object));
        response.getWriter().flush();
    }

    private static class Message {
        private final String user;
        private final String text;

        private Message(String user, String text) {
            this.user = user;
            this.text = text;
        }
    }
}