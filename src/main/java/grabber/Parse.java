package grabber;

import ru.job4j.utils.Post;

import java.util.List;

/**
 * 2.4. HabrCareerParse [#285213]
 */
public interface Parse {
    List<Post> list(String link);
}
