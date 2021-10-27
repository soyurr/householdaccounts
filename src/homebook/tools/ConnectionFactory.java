package homebook.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
//JDBC 1,2,4단계 커버(드라이버로딩단계,Connection객체생성단계, 자원반납단계)
// 싱글톤 패턴 ~ 오직 하나의 객체만 만들어 주는 클래스 패턴 
// 하나의 응용프로그램에서는 오직 하나의 통로만을 활용하는 것이 이점이 크다.
public class ConnectionFactory {
	private static Connection conn;
	// 예외를 전가시키는 이유 ~ 해당 예외 발생을 클라이언트가 인식하게끔 
	public synchronized static Connection create() throws SQLException{
		// 1,2 단계 
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String user ="homebook"; 
		String password ="homebook";
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.err.println("JDBC 드라이버가 없습니다.");
			e.printStackTrace();
		}
		conn = DriverManager.getConnection(url,user,password);
		return conn;
	} 
	// 4 단계 
	public synchronized static void close() throws SQLException {
		if(conn != null && !conn.isClosed()) {
			conn.close();
		}
	}
}
