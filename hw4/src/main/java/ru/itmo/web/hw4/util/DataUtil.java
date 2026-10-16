package ru.itmo.web.hw4.util;

import ru.itmo.web.hw4.model.Post;
import ru.itmo.web.hw4.model.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DataUtil {
    private static final String LONG_TEXT =
        "This round is held on the tasks of the school stage All-Russian Olympiad of Informatics 2018/2019" +
            "year in city Saratov. The problems were prepared by PikMike, fcspartakm, Ne0n25, BledDest, Ajosteen" +
            "and Vovuh. Great thanks to our coordinator _kun_ for the help with the round preparation! I also" +
            "would like to thank our testers DavidDenisov, PrianishnikovaRina, Decibit and Vshining." +
            "would like to thank our testers DavidDenisov, PrianishnikovaRina, Decibit and Vshining.";

    private static final List<User> USERS = Arrays.asList(
        new User(1, "MikeMirzayanov", "Mike Mirzayanov", User.Color.GREEN),
        new User(6, "pashka", "Pavel Mavrin", User.Color.RED),
        new User(9, "geranazavr555", "Georgiy Nazarov", User.Color.BLUE),
        new User(11, "tourist", "Gennady Korotkevich", User.Color.RED)
    );

    private static final List<Post> POSTS = Arrays.asList(
        new Post(1, "1", "Hello everyone!", 1),
        new Post(2, "Привет", "Пока!", 11),
        new Post(3, "Two", LONG_TEXT, 6)
    );

    public static void addData(HttpServletRequest request, Map<String, Object> data) {
        data.put("uri", request.getRequestURI());
        data.put("users", USERS);
        data.put("posts", POSTS);

        for (User user : USERS) {
            if (Long.toString(user.getId()).equals(request.getParameter("logged_user_id"))) {
                data.put("user", user);
            }
        }
    }
}