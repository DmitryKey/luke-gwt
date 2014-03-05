package com.totsp.sample.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import com.totsp.sample.client.model.DataException;
import com.totsp.sample.client.model.MyService;
import com.totsp.sample.client.model.Entry;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.sql.DataSource;

/**
 * Service Impl class, this is the GWT SERVER side code, runs on the server, not
 * bound by the client side JRE emulation library. In this case getting a
 * DataSource from Tomcat and storing stuff, in "real life" would pass off such
 * to your DAO or other and just be a "wrapper" with no logic.
 * 
 * @author ccollins
 * 
 */
public class MyServiceImpl extends RemoteServiceServlet implements MyService {    
    
    
    public List<Entry> myMethod(String s) throws DataException {
        Context initContext = null;
        Context envContext = null;
        DataSource ds = null;
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        List<Entry> resultList = new ArrayList<Entry>();

        // get a datasource from Tomcat 
        // (must be configured in context.xml and web.xml, 
        // see those files [/web/META-INF/context.xml /web/WEB-INF/web.xml])
        try {
            initContext = new InitialContext();
            envContext = (Context) initContext.lookup("java:/comp/env");
            ds = (DataSource) envContext.lookup("jdbc/DataSource");
        } catch (NamingException e) {
            e.printStackTrace();
            throw new DataException("UNABLE TO GET DATASOURCE - \n" + e.getMessage());
        }

        // naively create the tables and store some stuff       
        try {
            conn = ds.getConnection();
            st = conn.createStatement();

            // try to create the tables, if this bombs just ignore it, tables may already exist 
            // (had IF EXISTS problems with HSQL?)
            try {
                st.executeUpdate("CREATE TABLE sample ( id INTEGER IDENTITY, name VARCHAR(256), time VARCHAR(256))");
            } catch (SQLException e) {
                // swallow hard, gulp
            }

            // store the users name and Date in next row of table
            st.executeUpdate("INSERT INTO sample (name, time) VALUES ('" + s + "', '" + new Date() + "')");
            conn.commit();

            // return a List of all the stuff currently stored in the DB (this is a silly example remember)
            rs = st.executeQuery("SELECT * FROM sample");
            while (rs.next()) {
                String name = rs.getString("name");
                String time = rs.getString("time");
                Entry entry = new Entry();
                entry.name = name;
                entry.time = time;
                resultList.add(entry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataException("SQLException - " + e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (st != null) {
                    st.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return resultList;
    }
}
