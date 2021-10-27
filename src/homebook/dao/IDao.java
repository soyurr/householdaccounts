package homebook.dao;

import java.sql.SQLException;
import java.util.List;

public interface IDao<T,K> {
	public int insert(T vo) throws SQLException;
	public int delete(K key) throws SQLException;
	public int update(T vo) throws SQLException;
	public T select(K key) throws SQLException;
	public List<T> selectAll() throws SQLException;
	public List<T> selectByConditions(String conditions) throws SQLException;
}
