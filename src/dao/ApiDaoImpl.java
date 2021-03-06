package dao;

import java.sql.*;
import java.util.ArrayList;

import db.DB;
import dto.ApiDto;

public class ApiDaoImpl implements ApiDao{

	@Override
	public void insert(ApiDto dto) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = DB.conn();
			String sql = "INSERT INTO api (title, originallink, link, description, pubDate) VALUES (?, ?, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			
			// 4. 데이터 binding
			pstmt.setString(1, dto.getTitle());
			pstmt.setString(2, dto.getOriginallink());
			pstmt.setString(3, dto.getLink());
			pstmt.setString(4, dto.getDescription());
			pstmt.setString(5, dto.getPubDate());
//			pstmt.setString(1, dto.getEmail());
//			pstmt.setString(2, dto.getPw());
//			pstmt.setString(3, dto.getPnb());
//			pstmt.setString(4, dto.getName());
			
			int count = pstmt.executeUpdate();
			if (count == 0) {
				System.out.println("데이터 입력 실패");
			} else {
				System.out.println("데이터 입력 성공");
			}

		} catch (Exception e) {
			System.out.println("에러: " + e);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
				if( pstmt != null && !pstmt.isClosed()){
                    pstmt.close();
                }
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public ArrayList<ApiDto> select() {
		
		return null;
	}

}
