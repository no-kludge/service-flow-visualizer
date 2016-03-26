package aspect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Aspect
public class PublicAspect {
	private static final String SERVICE_NAME = "Service";
	Connection connection = null;
	PreparedStatement preparedStatement = null;
	Statement statement = null;
	static int seq = -1;
	static int invocationId = -1;
	
	@PostConstruct
	private void init() {
		try {
			System.out.println("in init");
			Class.forName("com.mysql.jdbc.Driver");
			
			connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/?user=root&password=root123");
			
			preparedStatement = connection.prepareStatement("insert into seq_viz.data(invocation_id, seq_no, class, api, side, service) values (?,?,?,?,?,?)");
			statement = connection.createStatement(); 
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Before(value = "execution(public * *(..))") 
	private void anyPublicOperation(JoinPoint jp) {
		String toClass = jp.getTarget().getClass().getSimpleName();
		String[] signatureParts = jp.getSignature().toString().split(" ");
		String api = signatureParts[0] + " " + jp.getSignature().getName();
		
		String className = Thread.currentThread().getStackTrace()[17].getClassName();
		int index = className.lastIndexOf('.');//.split(".");
		String fromClass =  className.substring(index+1);
		

		if(preparedStatement == null) 
			init();
		
		invocationId = invocationId==-1? getInvocationId() : invocationId;
		seq = seq==-1? getSeqId() : seq;
		 
		try {
			Annotation[] annotations = Class.forName(className).getAnnotations();
			 
			for(Annotation a : annotations) {
				if(a.annotationType().equals(RestController.class)) {
					List<String> prevData = getPrevClass(invocationId);
		
					insertToDb(invocationId, seq, prevData.get(0), api, "from", prevData.get(1));
					insertToDb(invocationId, seq++, fromClass, api, "to", "");
					
					break;
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
						
		 insertToDb(invocationId, seq, fromClass, api, "from", "");
		 insertToDb(invocationId, seq++, toClass, api, "to", "");	
	}

	private List<String> getPrevClass(int invocationId) {
		System.out.println("in getPrevClass");
		try {
			ResultSet rs = statement.executeQuery("select class, service from seq_viz.data where invocation_id=(select Max(invocation_id) from seq_viz.data)");
			
			rs.next();
			
			List<String> list = new ArrayList<String>();
			list.add(rs.getString("class"));
			list.add(rs.getString("service"));
			
			return list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private void insertToDb(int invocationId, int seq, String toClassName, String api, String side, String serviceName) {
		try {
			preparedStatement.setInt(1, invocationId);
			preparedStatement.setInt(2, seq);
			preparedStatement.setString(3, toClassName);
			preparedStatement.setString(4, api);
			preparedStatement.setString(5, side);
			preparedStatement.setString(6, serviceName.equals("") ? SERVICE_NAME : serviceName);
									
			preparedStatement.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	private int getInvocationId() {
		System.out.println("in invocation id");
		try {
			ResultSet rs = statement.executeQuery("select MAX(invocation_id) from seq_viz.data");
			
			rs.next();
			
			int invocation_id = rs.getInt(1);
			
			System.out.println("invocation id: " + invocation_id);
			return invocation_id;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;		
	}
	
	private int getSeqId() {
		System.out.println("in invocation id");
		try {
			ResultSet rs = statement.executeQuery("select MAX(seq_no) from seq_viz.data where invocation_id=" + invocationId);
			
			rs.next();
			
			int seq_id = rs.getInt(1);
			
			System.out.println("invocation id: " + seq_id);
			return ++seq_id;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;		
	}
	
	@PreDestroy
	private void destroy() {
		System.out.println("in destroy");
		try {
			if(preparedStatement != null) {
				preparedStatement.close();
				statement.close();
				connection.close();
			}
			seq = -1;
			invocationId = -1;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@After("execution(public * *(..))")
	public void advice(JoinPoint jp) {
		MethodSignature signature = (MethodSignature) jp.getSignature();
		Method method = signature.getMethod();
		   
		System.out.println("in controller");
		
		if(method.getName().toString().equals("getMessage")) {
			System.out.println("calling destroy;");
			destroy();
		}
	}
}