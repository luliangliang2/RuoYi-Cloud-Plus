package org.ssssssss.magicapi.hive.util;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.ssssssss.magicapi.hive.model.HiveInfo;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class HiveDataSource {

	private String id = "";
	private HikariDataSource dataSource;

	public HiveDataSource(HiveInfo info) throws Exception {
		this.id = info.getId();

		HikariConfig config = new HikariConfig();

		// 设置Hive JDBC URL
		String url = info.getUrl();
		if (info.getDatabase() != null && !info.getDatabase().isEmpty()) {
			if (!url.contains(";db=") && !url.contains("?db=")) {
				url += ";db=" + info.getDatabase();
			}
		}
		config.setJdbcUrl(url);

		// 设置认证信息
		if (info.getUsername() != null) {
			config.setUsername(info.getUsername());
		}
		if (info.getPassword() != null) {
			config.setPassword(info.getPassword());
		}

		// 设置连接池配置
		config.setDriverClassName("org.apache.hive.jdbc.HiveDriver");
		config.setMaximumPoolSize(10);
		config.setMinimumIdle(2);
		config.setConnectionTimeout(30000);
		config.setIdleTimeout(600000);
		config.setMaxLifetime(1800000);

		// Hive特定的配置
		config.addDataSourceProperty("hive.server2.transport.mode", "http");
		config.addDataSourceProperty("hive.server2.thrift.http.path", "cliservice");

		this.dataSource = new HikariDataSource(config);
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public String getId() {
		return id;
	}

	public boolean testConnection() {
		try (Connection conn = dataSource.getConnection()) {
			return conn != null && !conn.isClosed();
		} catch (SQLException e) {
			return false;
		}
	}

	public void close() {
		try {
			if (dataSource != null && !dataSource.isClosed()) {
				dataSource.close();
			}
		} catch (Exception e) {
			System.err.println("关闭连接时出错: " + e.getMessage());
		}
	}
}
