//package com.ljp.spring.batchtest.service;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.dao.DataAccessException;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.PreparedStatementCallback;
//import org.springframework.jdbc.core.PreparedStatementSetter;
//import org.springframework.jdbc.core.RowMapper;
//import org.springframework.stereotype.Service;
//
//import com.ljp.spring.batchtest.TMPPerson;
//import com.mysql.jdbc.PreparedStatement;
//
//@Service()
//public class PersonService {
//	@Autowired
//    private JdbcTemplate jdbcTemplate;
//	
//	private final String GET_ONE_PERSON = "select * from Person where person_name = ?";
//	private final String PERSON_INSERT = "INSERT INTO Person (person_name, person_age,person_sex) VALUES (?,?,?)";
//	
//	public TMPPerson getOnePerson(TMPPerson person){
//		TMPPerson selected_person = new TMPPerson();
//		List<TMPPerson> personList = jdbcTemplate.query(GET_ONE_PERSON, new Object[] {person.getPersonName()}, new RowMapper<TMPPerson>() {
//            @Override
//            public TMPPerson mapRow( ResultSet resultSet, int rowNum ) throws SQLException {
//            	TMPPerson p = new TMPPerson();
//            	p.setPersonName(resultSet.getString(1));
//            	p.setPersonAge(resultSet.getString(2));
//            	p.setPersonSex(resultSet.getString(3));
//                return p;
//            }
//        });
//		
//		if(personList.size() > 0){
//			selected_person =  personList.get(0);
//		}
//		return selected_person;
//		
//	}
//	
//	public boolean saveOnePerson(TMPPerson person){
//		boolean savedFlag = true;
//		try{
//			jdbcTemplate.update(PERSON_INSERT, new Object[]{person.getPersonName(), person.getPersonAge(), person.getPersonSex()});
//		}catch(Exception e){
//			savedFlag = false;
//		}
//		
//		return savedFlag;
//		
//	}
//	
//	public TMPPerson pullOnePerson(){
//		TMPPerson person = new TMPPerson();
//		person.setPersonAge("20");
//		person.setPersonId(10000);
//		person.setPersonName("Mario");
//		person.setPersonSex("0");
//		
//		return person;
//	}
//
//	
//
//}
