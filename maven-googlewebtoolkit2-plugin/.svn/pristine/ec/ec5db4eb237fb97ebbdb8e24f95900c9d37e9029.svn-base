package com.totsp.sample.util;

import org.hsqldb.jdbcDriver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Stupid simple, naive, never do this type JDBC example, just to test HSQL.
 * 
 * @author ccollins
 * 
 */
public class DBConnTest {
    private static final String JDBC_URL = "jdbc:hsqldb:mem:testdb";
    private static final String JDBC_DRIVER = "org.hsqldb.jdbcDriver";
    private static final String JDBC_USER = "sa";
    private static final String JDBC_PASS = "";

    public static void testConnection() {
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            Class.forName(JDBC_DRIVER);
            DriverManager.registerDriver(new jdbcDriver());
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);

            st = conn.createStatement();

            // create a table to do something with
            st.executeUpdate("CREATE TABLE sample ( id INTEGER IDENTITY, name VARCHAR(256), time VARCHAR(256))");
            conn.commit();
            System.out.println("created sample table");

            // put some stuff IN the table
            st.executeUpdate("INSERT INTO sample (name, time) VALUES ('test1', 'party like its 1999')");
            st.executeUpdate("INSERT INTO sample (name, time) VALUES ('test2', 'the time, what time is it')");
            st.executeUpdate("INSERT INTO sample (name, time) VALUES ('test3', 'time flies')");
            conn.commit();
            System.out.println("added some stuff to the sample table");

            // get some stuff OUT of the table
            rs = st.executeQuery("SELECT * FROM sample");
            System.out.println("getting stuff from sample table \n");
            System.out.println(DBConnTest.display(rs));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                st.close();
                conn.close();
            } catch (Exception e) {
                // gulp
            }
        }
    }

    public static String display(ResultSet rs) {
        StringBuffer sb = new StringBuffer();

        try {
            ResultSetMetaData meta = rs.getMetaData();
            int colmax = meta.getColumnCount();
            int i;
            Object o = null;

            while (rs.next()) {
                for (i = 0; i < colmax; ++i) {
                    o = rs.getObject(i + 1);
                    sb.append(o.toString() + " \n");
                }

                sb.append(" \n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println("call testConnection");
        DBConnTest.testConnection();
    }
}
