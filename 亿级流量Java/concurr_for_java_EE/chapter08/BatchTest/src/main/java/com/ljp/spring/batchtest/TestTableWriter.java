package com.ljp.spring.batchtest;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class TestTableWriter implements ItemWriter<String> {
	  
    private JdbcTemplate jdbcTemplate;  

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}


	@Override
	public  void write(List<? extends String> indexVals) throws Exception {
		System.out.println("-------tx step writer--write()-------");
		
		for(String tmpIndex : indexVals){
			String key = "key_" + tmpIndex;
			String value = "value_" + tmpIndex;
			
			jdbcTemplate.update("insert into test_tbl values('" + key + "','" + value + "')");
		}	
		
	}
}
