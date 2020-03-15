package simplejdbc;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestSimpleJdbc {

    @Test
    public void testSimleJdbcSample() throws SQLException {
        var dao = new SimpleJdbc();
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
    }

    @Test
    public void testErrors() {
        assertThrows(SQLException.class, () -> {
            var dao = new SimpleJdbc();
            dao.initSchema();
            dao.addName(100, "Hello");
            dao.addName(100, "Salut");
        });
    }

    @Test
    public void testUpdateName() throws SQLException {
        var dao = new SimpleJdbc();
        dao.initSchema();
        dao.deleteName(100);
        dao.addName(100, "Hello");
        dao.updateName(100, "Coucou");
        assertEquals("Coucou", dao.findName(100));
        System.out.printf("name=%s\n", dao.findName(100));
    }

    @Test
    public void testSQLInjection() throws SQLException {
        var dao = new SimpleJdbc();
        dao.initSchema();
        dao.deleteName(100);
        dao.addName(100, "Hello");
        dao.updateName(100, "Coucou; Update NAME set name = 'johnny';");
        assertEquals("Coucou; Update NAME set name = 'johnny';", dao.findName(100));
        System.out.printf("name=%s\n", dao.findName(100));
    }

}