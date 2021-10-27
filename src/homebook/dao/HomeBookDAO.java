package homebook.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import homebook.tools.ConnectionFactory;
import homebook.vo.HomeBook;

public class HomeBookDAO implements IDao<HomeBook, Long> {

	@Override
	public int insert(HomeBook vo) throws SQLException {
		Connection conn = ConnectionFactory.create();
		String sql = "INSERT INTO HOMEBOOK " + "(SERIALNO,DAY,SECTION,ACCOUNTTITLE,REMARK,REVENUE,EXPENSE) "
				+ "VALUES(SEQ_HOMEBOOK.NEXTVAL,?,?,?,?,?,?)";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setTimestamp(1, vo.getDay());
		pstmt.setString(2, vo.getSection());
		pstmt.setString(3, vo.getAccounttitle());
		pstmt.setString(4, vo.getRemark());
		pstmt.setLong(5, vo.getRevenue());
		pstmt.setLong(6, vo.getExpense());
		int res = pstmt.executeUpdate();
		conn.close();
		return res;
	}

	@Override
	public int delete(Long key) throws SQLException {
		Connection conn = ConnectionFactory.create();
		String sql = "DELETE FROM HOMEBOOK WHERE SERIALNO = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setLong(1, key);
		int res = pstmt.executeUpdate();
		conn.close();
		return res;
	}

	@Override
	public int update(HomeBook vo) throws SQLException {
		Connection conn = ConnectionFactory.create();
		String sql = "UPDATE HOMEBOOK SET DAY=?,SECTION=?,ACCOUNTTITLE=?, " 
		+ "REMARK=?, REVENUE=?, EXPENSE=? "
				+ "WHERE SERIALNO=? ";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setTimestamp(1, vo.getDay());
		pstmt.setString(2, vo.getSection());
		pstmt.setString(3, vo.getAccounttitle());
		pstmt.setString(4, vo.getRemark());
		pstmt.setLong(5, vo.getRevenue());
		pstmt.setLong(6, vo.getExpense());
		pstmt.setLong(7, vo.getSerialno());
		int res = pstmt.executeUpdate();
		conn.close();
		return res;
	}

	@Override
	public HomeBook select(Long key) throws SQLException {
		Connection conn = ConnectionFactory.create();
		String sql = "SELECT * FROM HOMEBOOK WHERE SERIALNO=?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setLong(1, key);
		ResultSet rs = pstmt.executeQuery();
		HomeBook vo = new HomeBook();
		while (rs.next()) {
			// 컬럼이름으로 가져오는 것이 더 안전함
			vo.setSerialno(rs.getLong("SERIALNO")); // 1
			vo.setDay(rs.getTimestamp("DAY")); // 2
			vo.setSection(rs.getString("SECTION"));
			vo.setAccounttitle(rs.getString("ACCOUNTTITLE"));
			vo.setRemark(rs.getString("REMARK"));
			vo.setRevenue(rs.getLong("REVENUE"));
			vo.setExpense(rs.getLong("EXPENSE"));
		}
		conn.close();
		return vo;
	}

	@Override
	public List<HomeBook> selectAll() throws SQLException {
		Connection conn = ConnectionFactory.create();
		String sql = "SELECT * FROM HOMEBOOK ";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		List<HomeBook> data = new ArrayList<>();
		while (rs.next()) {
			HomeBook vo = new HomeBook();
			// 컬럼이름으로 가져오는 것이 더 안전함
			vo.setSerialno(rs.getLong("SERIALNO")); // 1
			vo.setDay(rs.getTimestamp("DAY")); // 2
			vo.setSection(rs.getString("SECTION"));
			vo.setAccounttitle(rs.getString("ACCOUNTTITLE"));
			vo.setRemark(rs.getString("REMARK"));
			vo.setRevenue(rs.getLong("REVENUE"));
			vo.setExpense(rs.getLong("EXPENSE"));
			data.add(vo);
		}
		conn.close();
		return data;
	}

	@Override
	public List<HomeBook> selectByConditions(String conditions) throws SQLException {
		Connection conn = ConnectionFactory.create();
		String sql = "SELECT * FROM HOMEBOOK " + conditions;
		PreparedStatement pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		List<HomeBook> data = new ArrayList<>();
		while (rs.next()) {
			HomeBook vo = new HomeBook();
			// 컬럼이름으로 가져오는 것이 더 안전함
			vo.setSerialno(rs.getLong("SERIALNO")); // 1
			vo.setDay(rs.getTimestamp("DAY")); // 2
			vo.setSection(rs.getString("SECTION"));
			vo.setAccounttitle(rs.getString("ACCOUNTTITLE"));
			vo.setRemark(rs.getString("REMARK"));
			vo.setRevenue(rs.getLong("REVENUE"));
			vo.setExpense(rs.getLong("EXPENSE"));
			data.add(vo);
		}
		conn.close();
		return data;
	}

	public long getMax() throws SQLException {
		long max = 0;
		Connection conn = ConnectionFactory.create();
		String sql = "SELECT MAX(SERIALNO) FROM HOMEBOOK ";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		rs.next();
		max = rs.getLong(1);
		return max;
	}
}
