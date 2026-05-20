package kr.or.ddit.util;

import java.io.Reader;
import java.nio.charset.Charset;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import jakarta.annotation.Resource;

public class MybatisUtil {

	private static SqlSessionFactory sessionfactory;
	
	static {
		try {
			Charset charset = Charset.forName("UTF-8");
			//한글 꺠짐방지용 인코딩...
			Resources.setCharset(charset);
			
			Reader rd= Resources.getResourceAsReader("config/MyBatisConfig.xml");
			//Reader객체 생성하여 xml환경설정 읽기 ....
			
			sessionfactory = new SqlSessionFactoryBuilder().build(rd);
			//읽은 환경설정의 rd객체를 이용하여 sqlsessionFactory 생성.... 
			
			rd.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static SqlSession getsqlsession() {
		return sessionfactory.openSession();
		//해당 메서드 호출시 sqlsession세션 획득(오토커밋여부)		
	}
	public static SqlSession getsqlsession(boolean commit) {
		return sessionfactory.openSession(commit);
	}
		//해당 메서드 호출시 sqlsession세션 획득(오토커밋여부)	
	
	
}
