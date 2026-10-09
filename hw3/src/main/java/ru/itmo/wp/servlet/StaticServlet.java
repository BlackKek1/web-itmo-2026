package ru.itmo.wp.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StaticServlet extends HttpServlet {
    private static final String STATIC_DIR = "src/main/webapp/static";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] uris = request.getRequestURI().split("\\+");
        List<File> files = findFiles(uris);
        if (!files.isEmpty()) {
            response.setContentType(getServletContext().getMimeType(files.get(0).getName()));
            try (OutputStream outputStream = response.getOutputStream()) {
                for (File file : files) {
                    Files.copy(file.toPath(), outputStream);
                }
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private List<File> findFiles(String[] uris) throws IOException {
        List<File> files = new ArrayList<>();
        for (String uri : uris) {
            File file = findFile(uri);
            if (file == null) {
                return Collections.emptyList();
            }
            files.add(file);
        }
        return files;
    }

    private File findFile(String uri) throws IOException {
        File file = findFileInDirectory(new File(STATIC_DIR), uri);
        if (file == null) {
            file = findFileInDirectory(new File(getServletContext().getRealPath("/static")), uri);
        }
        return file;
    }

    private File findFileInDirectory(File directory, String uri) throws IOException {
        File file = new File(directory, uri).getCanonicalFile();
        boolean isInsideDirectory = file.toPath().startsWith(directory.getCanonicalFile().toPath());
        return isInsideDirectory && file.isFile() ? file : null;
    }
}