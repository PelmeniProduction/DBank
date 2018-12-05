package com.example.demo1;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;

@Component
public class AnalyticService {

    @Autowired
    ConnectionManager connectionManager;

    private volatile List<Object> cachedData = Collections.emptyList();

    @Scheduled
    public void reloadData() throws Exception {
        Connection connection = connectionManager.getConnection();

    }

    public List<Object> getCachedData() {
        return cachedData;
    }
}
