package start;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;

public class DbUtil {
	Logger log = LoggerFactory.getLogger(DbUtil.class);

	private DbUtil() {
	}

	private static SqlSessionFactory sqlSessionFactory;

	static {
		String resource = "dal/mybatis-config.xml";
		Reader reader = null;
		try {
			reader = Resources.getResourceAsReader(resource);
		} catch (IOException e) {
			e.printStackTrace();
		}
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
	}

	public static SqlSessionFactory getSessionFactory() {
		return sqlSessionFactory;
	}
}