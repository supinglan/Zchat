package pers.spl.chatserver.datasource;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DbcpConnectionPool {

    private BasicDataSource dataSource;

    public DbcpConnectionPool() {
        dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/chat?useSSL=false&serverTimezone=Asia/Shanghai");
        dataSource.setUsername("spl");
        dataSource.setPassword("zju20230");
        dataSource.setInitialSize(5); // 初始连接数
        dataSource.setMaxTotal(10); // 最大连接数
        dataSource.setMaxIdle(5); // 最大空闲连接
        dataSource.setMinIdle(2); // 最小空闲连接
        dataSource.setMaxWaitMillis(1000); // 等待连接的最大时间（毫秒）
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

}
