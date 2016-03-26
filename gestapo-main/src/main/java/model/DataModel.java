package model;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import dao.Data;
import dao.DataDAOImpl;

public class DataModel {
	public static List<Data> getData() {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		
		DataDAOImpl dao = ctx.getBean("dataDaoImpl", DataDAOImpl.class);

		List<Data> data = dao.getData();
		
//		JSONArray arr = new JSONArray(data);
		
		return data;
	}
}
