package org.ssssssss.magicapi.modules.db.dialect;

import org.ssssssss.magicapi.modules.db.BoundSql;

public class TDengineDialect implements Dialect {
	
	
	public boolean match(String jdbcUrl) {
		return jdbcUrl.contains(":TAOS-RS:") || jdbcUrl.contains(":taos-rs:");
	}

	public String getPageSql(String sql, BoundSql boundSql, long offset, long limit) {
		boundSql.addParameter(offset);
		boundSql.addParameter(limit);
		return sql + "\n limit ?,?";
	}

}
