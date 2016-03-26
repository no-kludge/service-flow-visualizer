package dao;

import java.util.List;

import javax.sql.DataSource;

public interface DataDAO {
	public List<Data> getData();
	
	public void setDataSource(DataSource ds);
}
