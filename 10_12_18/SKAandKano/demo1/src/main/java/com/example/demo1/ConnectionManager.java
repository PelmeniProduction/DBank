package com.example.demo1;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;

@Component
public class ConnectionManager {



    public Connection getConnection1() throws Exception {
        Class.forName("org.postgresql.Driver");
        Connection c = DriverManager
                .getConnection("jdbc:postgresql://localhost:5432/gold",
                        "postgres", "postgres");
        c.setAutoCommit(false);
        return c;

    }
}
