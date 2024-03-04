package pers.spl.chatserver.datasource;

import org.apache.commons.dbcp2.BasicDataSource;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DbcpConnectionPool {

    private BasicDataSource dataSource;

    public DbcpConnectionPool() {
        dataSource = new BasicDataSource();
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream("src/main/resources/db.properties")) {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        dataSource.setUrl(properties.getProperty("db.url"));
        dataSource.setUsername(properties.getProperty("db.username"));
        dataSource.setPassword(properties.getProperty("db.password"));
        dataSource.setInitialSize(Integer.parseInt(properties.getProperty("db.initialSize")));
        dataSource.setMaxTotal(Integer.parseInt(properties.getProperty("db.maxTotal")));
        dataSource.setMaxIdle(Integer.parseInt(properties.getProperty("db.maxIdle")));
        dataSource.setMinIdle(Integer.parseInt(properties.getProperty("db.minIdle")));
        dataSource.setMaxWaitMillis(Long.parseLong(properties.getProperty("db.maxWaitMillis")));
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

}
