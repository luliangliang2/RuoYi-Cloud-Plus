package org.ssssssss.magicapi.hive;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ssssssss.script.annotation.Comment;
import org.ssssssss.magicapi.core.annotation.MagicModule;

@MagicModule("hive")
public class HiveModule {

	// 默认的Hive连接信息，可以通过配置覆盖
	private String defaultUrl = "jdbc:hive2://localhost:10000/default";
	private String defaultUsername = "";
	private String defaultPassword = "";

	public HiveModule() {
		// 默认构造函数
	}

	public HiveModule(String url, String username, String password) {
		this.defaultUrl = url;
		this.defaultUsername = username;
		this.defaultPassword = password;
	}

	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection(defaultUrl, defaultUsername, defaultPassword);
	}

	@Comment("/**\n\n"
			+ "	* 执行Hive SQL查询语句\n\n"
			+ "	* @param sql SQL查询语句\n\n"
			+ "	* @return 查询结果列表，每个Map代表一行数据\n\n"
			+ "	* @throws SQLException\n\n"
			+ "	*\n\n"
			+ "	*	hive.select(\"SELECT * FROM table_name LIMIT 10\");\n\n"
			+ "	*/")
	public List<Map<String, Object>> select(@Comment(name = "sql", value = "SQL查询语句") String sql) throws SQLException {
		try (Connection conn = getConnection();
			 Statement stmt = conn.createStatement();
			 ResultSet rs = stmt.executeQuery(sql)) {

			List<Map<String, Object>> results = new ArrayList<>();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();

			while (rs.next()) {
				Map<String, Object> row = new HashMap<>();
				for (int i = 1; i <= columnCount; i++) {
					String columnName = metaData.getColumnName(i);
					Object value = rs.getObject(i);
					row.put(columnName, value);
				}
				results.add(row);
			}

			return results;
		}
	}

	@Comment("/**\n\n"
			+ "	* 执行Hive SQL查询语句（带参数）\n\n"
			+ "	* @param sql SQL查询语句，使用?作为占位符\n\n"
			+ "	* @param params 参数列表\n\n"
			+ "	* @return 查询结果列表，每个Map代表一行数据\n\n"
			+ "	* @throws SQLException\n\n"
			+ "	*\n\n"
			+ "	*	hive.select(\"SELECT * FROM table_name WHERE id = ?\", 123);\n\n"
			+ "	*/")
	public List<Map<String, Object>> select(@Comment(name = "sql", value = "SQL查询语句") String sql,
										   @Comment(name = "params", value = "参数列表") Object... params) throws SQLException {
		try (Connection conn = getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {

			// 设置参数
			for (int i = 0; i < params.length; i++) {
				stmt.setObject(i + 1, params[i]);
			}

			try (ResultSet rs = stmt.executeQuery()) {
				List<Map<String, Object>> results = new ArrayList<>();
				ResultSetMetaData metaData = rs.getMetaData();
				int columnCount = metaData.getColumnCount();

				while (rs.next()) {
					Map<String, Object> row = new HashMap<>();
					for (int i = 1; i <= columnCount; i++) {
						String columnName = metaData.getColumnName(i);
						Object value = rs.getObject(i);
						row.put(columnName, value);
					}
					results.add(row);
				}

				return results;
			}
		}
	}

	@Comment("/**\n\n"
			+ "	* 执行Hive SQL更新语句（INSERT、UPDATE、DELETE）\n\n"
			+ "	* @param sql SQL更新语句\n\n"
			+ "	* @return 影响的行数\n\n"
			+ "	* @throws SQLException\n\n"
			+ "	*\n\n"
			+ "	*	hive.execute(\"INSERT INTO table_name VALUES ('value1', 'value2')\");\n\n"
			+ "	*/")
	public int execute(@Comment(name = "sql", value = "SQL更新语句") String sql) throws SQLException {
		try (Connection conn = getConnection();
			 Statement stmt = conn.createStatement()) {
			return stmt.executeUpdate(sql);
		}
	}

	@Comment("/**\n\n"
			+ "	* 执行Hive SQL更新语句（带参数）\n\n"
			+ "	* @param sql SQL更新语句，使用?作为占位符\n\n"
			+ "	* @param params 参数列表\n\n"
			+ "	* @return 影响的行数\n\n"
			+ "	* @throws SQLException\n\n"
			+ "	*\n\n"
			+ "	*	hive.execute(\"INSERT INTO table_name VALUES (?, ?)\", \"value1\", \"value2\");\n\n"
			+ "	*/")
	public int execute(@Comment(name = "sql", value = "SQL更新语句") String sql,
					  @Comment(name = "params", value = "参数列表") Object... params) throws SQLException {
		try (Connection conn = getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {

			// 设置参数
			for (int i = 0; i < params.length; i++) {
				stmt.setObject(i + 1, params[i]);
			}

			return stmt.executeUpdate();
		}
	}

	@Comment("/**\n\n"
			+ "	* 批量执行Hive SQL语句\n\n"
			+ "	* @param sqls SQL语句列表\n\n"
			+ "	* @return 每条SQL语句影响的行数数组\n\n"
			+ "	* @throws SQLException\n\n"
			+ "	*\n\n"
			+ "	*	hive.batchExecute([\"INSERT INTO t VALUES(1)\", \"INSERT INTO t VALUES(2)\"]);\n\n"
			+ "	*/")
	public int[] batchExecute(@Comment(name = "sqls", value = "SQL语句列表") List<String> sqls) throws SQLException {
		try (Connection conn = getConnection();
			 Statement stmt = conn.createStatement()) {

			for (String sql : sqls) {
				stmt.addBatch(sql);
			}

			return stmt.executeBatch();
		}
	}

	@Comment("/**\n\n"
			+ "	* 创建Hive表\n\n"
			+ "	* @param createTableSql CREATE TABLE语句\n\n"
			+ "	* @throws SQLException\n\n"
			+ "	*\n\n"
			+ "	*	hive.createTable(\"CREATE TABLE test_table (id INT, name STRING) STORED AS PARQUET\");\n\n"
			+ "	*/")
	public void createTable(@Comment(name = "createTableSql", value = "CREATE TABLE语句") String createTableSql) throws SQLException {
		execute(createTableSql);
	}

	@Comment("/**\n\n"
			+ "	* 删除Hive表\n\n"
			+ "	* @param tableName 表名\n\n"
			+ "	* @throws SQLException\n\n"
			+ "	*\n\n"
			+ "	*	hive.dropTable(\"test_table\");\n\n"
			+ "	*/")
	public void dropTable(@Comment(name = "tableName", value = "表名") String tableName) throws SQLException {
		execute("DROP TABLE IF EXISTS " + tableName);
	}

	@Comment("/**\n\n"
			+ "	* 显示所有表\n\n"
			+ "	* @return 表名列表\n\n"
			+ "	* @throws SQLException\n\n"
			+ "	*\n\n"
			+ "	*	hive.showTables();\n\n"
			+ "	*/")
	public List<String> showTables() throws SQLException {
		List<Map<String, Object>> results = select("SHOW TABLES");
		List<String> tables = new ArrayList<>();
		for (Map<String, Object> row : results) {
			tables.add(row.get("tableName") != null ? row.get("tableName").toString() :
					  (row.get("tab_name") != null ? row.get("tab_name").toString() : ""));
		}
		return tables;
	}

	@Comment("/**\n\n"
			+ "	* 描述表结构\n\n"
			+ "	* @param tableName 表名\n\n"
			+ "	* @return 表结构信息\n\n"
			+ "	* @throws SQLException\n\n"
			+ "	*\n\n"
			+ "	*	hive.describeTable(\"test_table\");\n\n"
			+ "	*/")
	public List<Map<String, Object>> describeTable(@Comment(name = "tableName", value = "表名") String tableName) throws SQLException {
		return select("DESCRIBE " + tableName);
	}

	@Comment("/**\n\n"
			+ "	* 获取表行数\n\n"
			+ "	* @param tableName 表名\n\n"
			+ "	* @return 行数\n\n"
			+ "	* @throws SQLException\n\n"
			+ "	*\n\n"
			+ "	*	hive.count(\"test_table\");\n\n"
			+ "	*/")
	public long count(@Comment(name = "tableName", value = "表名") String tableName) throws SQLException {
		List<Map<String, Object>> results = select("SELECT COUNT(*) as count FROM " + tableName);
		if (!results.isEmpty()) {
			Object count = results.get(0).get("count");
			return count != null ? Long.parseLong(count.toString()) : 0L;
		}
		return 0L;
	}

	@Comment("/**\n\n"
			+ "	* 执行任意Hive SQL语句\n\n"
			+ "	* @param sql 任意SQL语句\n\n"
			+ "	* @return 如果是查询语句返回结果列表，否则返回影响行数\n\n"
			+ "	* @throws SQLException\n\n"
			+ "	*\n\n"
			+ "	*	hive.executeSql(\"SELECT * FROM test_table\");\n\n"
			+ "	*	hive.executeSql(\"CREATE DATABASE test_db\");\n\n"
			+ "	*/")
	public Object executeSql(@Comment(name = "sql", value = "任意SQL语句") String sql) throws SQLException {
		String upperSql = sql.trim().toUpperCase();
		if (upperSql.startsWith("SELECT") || upperSql.startsWith("SHOW") || upperSql.startsWith("DESCRIBE")) {
			return select(sql);
		} else {
			return execute(sql);
		}
	}
}
