package com.example.demo1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.Date;

@Component
public class DataCrawler {

    @Autowired
    Configuration configuration;

    @Autowired
    ConnectionManager connectionManager;

    @Scheduled
    public void reloadData() throws Exception {
        Connection connection = connectionManager.getConnection();
        Date start = null; // DB.getLastUpdated()
        if (start == null) {
            start = new Date();// one week ago
        }

        Date end = new Date();
        //configuration.remoteServiceUrl

        //

        // DB.saveAll(connection, timeSeriesList);
        connection.commit();
        connection.close();
    }
}
