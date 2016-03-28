package com.gestapo.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class DataMapper implements RowMapper<Data> {
	public Data mapRow(ResultSet rs, int rowNum) throws SQLException {
		Data data = new Data();
		data.setInvocationId(rs.getInt("invocation_id"));
		data.setSeqNo(rs.getInt("seq_no"));
		data.setClassName(rs.getString("class"));
		data.setMethod(rs.getString("api"));
		data.setSide(rs.getString("side"));
		data.setService(rs.getString("service"));
		
		return data;
	}
}