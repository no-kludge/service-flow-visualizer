package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class DataDAOImpl implements DataDAO {
	
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplateObject;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcTemplateObject = new JdbcTemplate(dataSource);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Data> getData() {
		String SQL = "select * from seq_viz.data where invocation_id = (Select Max(invocation_id) from seq_viz.data)";
		
		String infoSQL = "select distinct seq_no, service, class from seq_viz.data order by seq_no";
		
		List<InfoData> infoData = jdbcTemplateObject.query(infoSQL, new RowMapper<InfoData>() {

			public InfoData mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				InfoData bean = new InfoData();
				
				bean.setClassName(rs.getString("class"));
				bean.setServiceName(rs.getString("service"));
				
				return bean;
			}} );
		
		List <Data> data = jdbcTemplateObject.query(SQL, new DataMapper());
		
		List ret = new ArrayList<List<?>>();
		ret.add(infoData);
		
		List<ReqData> reqData = processData(data);
		
		ret.add(reqData);
		return ret;
	}

	private List<ReqData> processData(List<Data> data) {
		List<ReqData> list = new ArrayList<ReqData>();
		
		int cntr = 1;
		ReqData rd = new ReqData();
		for(Data d : data) {			
			if("to".equals(d.getSide())) {
				rd.setToServiceName(d.getService());
				rd.setToClassName(d.getClassName());
			} else {
				rd.setFromServiceName(d.getService());
				rd.setFromClassName(d.getClassName());
			}
						
			rd.setMethSig(d.getMethod());
			rd.setSeqNo(d.getSeqNo());
			
			if(cntr%2 == 0) {
				list.add(rd);
				rd = new ReqData();
			}
			
			cntr++;
		}
		
		System.out.println(list);
		return list;
	}
}
