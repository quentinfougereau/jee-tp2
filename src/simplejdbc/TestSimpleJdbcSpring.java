package simplejdbc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SimpleJdbcConfig.class)
public class TestSimpleJdbcSpring {

    @Autowired
    SimpleJdbcSpring dao;

    @Test
    public void testSimleJdbcSample() throws SQLException {
        dao.initSchema();
        dao.deleteName(100);
        dao.deleteName(200);
        dao.addName(100, "Hello");
        dao.addName(200, "Salut");
        assertEquals("Hello", dao.findName(100));
        assertEquals("Salut", dao.findName(200));
        for (String name : dao.findNames()) {
            System.out.printf("name=%s\n", name);
        }
        /*dao.findNamesBeans().stream().forEach((Name n) -> {
            System.out.println(n);
        });*/
    }

    @Test
    public void testErrors() {
        assertThrows(DuplicateKeyException.class, () -> {
            dao.initSchema();
            dao.addName(100, "Hello");
            dao.addName(100, "Salut");
        });
    }

    @Test
    public void testFindNamesBeans() throws SQLException {
        dao.initSchema();
        dao.deleteName(100);
        dao.deleteName(200);
        dao.addName(100, "Hello");
        dao.addName(200, "Salut");
    }

    @Test
    public void testWorks() throws Exception {
        long debut = System.currentTimeMillis();

        // exécution des threads
        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 1; (i < 5); i++) {
            executor.execute(dao::longWork);
        }

        // attente de la fin des threads
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.HOURS);

        // calcul du temps de réponse
        long fin = System.currentTimeMillis();
        System.out.println("duree = " + (fin - debut) + "ms");
    }

}
