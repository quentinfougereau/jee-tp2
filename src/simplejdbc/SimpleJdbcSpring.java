package simplejdbc;

import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Service
public class SimpleJdbcSpring {

    @Autowired
    JdbcTemplate jt;

    final String url = "jdbc:mysql://localhost/testdb";
    final String user = "root";
    final String password = "root";

    private Connection newConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public void initSchema() throws SQLException {
        var query = "create table if not exists NAME (" //
                + " id integer not null, " //
                + " name varchar(50) not null, " //
                + " primary key (id) )";
        try (var conn = newConnection()) {
            conn.createStatement().execute(query);
        }
    }

    public void addName(int id, String name) throws SQLException {
        var query = "insert into NAME values (?,?)";
        try (var conn = newConnection()) {
            var st = conn.prepareStatement(query);
            st.setInt(1, id);
            st.setString(2, name);
            st.execute();
        }
    }

    public void deleteName(int id) throws SQLException {
        var query = "Delete From NAME where (id = ?)";
        try (var conn = newConnection()) {
            var st = conn.prepareStatement(query);
            st.setInt(1, id);
            st.execute();
        }
    }

    public String findName(int id) throws SQLException {
        var query = "Select * From NAME where (id = ?)";
        try (var conn = newConnection()) {
            var st = conn.prepareStatement(query);
            st.setInt(1, id);
            var rs = st.executeQuery();
            if (rs.next()) {
                return rs.getString("name");
            }
        }
        return null;
    }

    public Collection<String> findNames() throws SQLException {
        var query = "Select * From NAME order by name";
        var result = new LinkedList<String>();
        try (var conn = newConnection()) {
            var st = conn.createStatement();
            var rs = st.executeQuery(query);
            while (rs.next()) {
                result.add(rs.getString("name"));
            }
        }
        return result;
    }

    public void updateName(int id, String name) throws SQLException {
        var query = "Update NAME SET name = ? where (id = ?)";
        try (var conn = newConnection()) {
            var st = conn.prepareStatement(query);
            st.setString(1, name);
            st.setInt(2, id);
            st.execute();
        }
    }

    public List<Name> findNamesBeans() {
        var query = "Select * From NAME order by name";
        return jt.query(query, SimpleJdbcSpring::nameMapper);
    }

    private static Name nameMapper(ResultSet rs, int i) throws SQLException {
        var n = new Name();
        n.setId(rs.getInt("id"));
        n.setName(rs.getString("name"));
        return n;
    }

    public void addNameBean(int id, String name) throws SQLException {
        var query = "insert into NAME values (?,?)";
        jt.update(query, id, name);
    }

    public void deleteNameBean(int id) throws SQLException {
        var query = "Delete From NAME where (id = ?)";
        jt.update(query, id);
    }

    public void longWork() {
        try (var c = jt.getDataSource().getConnection()) {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        } catch (SQLException e1) {
        }
    }

}