package com.ljp.spring.batchtest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class TestTransaction {

    @Autowired  
    private JdbcTemplate jdbcTemplate;  
	
	@Transactional(value="transactionManager")
	public void testInsert() throws SQLException{

		
		jdbcTemplate.update("insert into test_tbl values('key01','value01')");
		jdbcTemplate.update("insert into test_tbl values('key02','value02 too long too long too long......')");
		jdbcTemplate.update("insert into test_tbl values('key03','value03')");
		
		
//		throw new RuntimeException( "Dao的时候手动抛出异常" );  

		
	}
}
