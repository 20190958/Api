package dao;

import java.util.ArrayList;

import dto.ApiDto;

public interface ApiDao {
public void insert(ApiDto dto);
	
	public ArrayList<ApiDto> select();
}
