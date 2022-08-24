//package com.ljp.spring.batchtest;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.List;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.batch.item.ItemProcessor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.RowMapper;
//
//import com.ljp.spring.batchtest.service.PersonService;
//
///**
// * 中间转换器
// * @author wbw
// *
// */
//
//public class TMPPersonItemProcessor implements ItemProcessor<TMPPerson, TMPPerson> {
//	//查询
//	//private static final String GET_PRODUCT = "select * from Person where person_name = ?";
//	
//    private static final Logger log = LoggerFactory.getLogger(TMPPersonItemProcessor.class);
//    
//    
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//    
//    @Autowired
//    private PersonService personService;
//    
//    
//    @Override
//    public TMPPerson process(final TMPPerson person) throws Exception {
////        List<Person> personList = jdbcTemplate.query(GET_PRODUCT, new Object[] {person.getPersonName()}, new RowMapper<Person>() {
////            @Override
////            public Person mapRow( ResultSet resultSet, int rowNum ) throws SQLException {
////            	Person p = new Person();
////            	p.setPersonName(resultSet.getString(1));
////            	p.setPersonAge(resultSet.getString(2));
////            	p.setPersonSex(resultSet.getString(3));
////                return p;
////            }
////        });
////        if(personList.size() >0){
////        	log.info("该数据已录入!!!");
////        }
//        System.out.println("------process-------");
//    	TMPPerson query_person = personService.getOnePerson(person);
//    	
//    	if(query_person.getPersonName() != null){
//    		log.info("该数据已录入!!!(通过新注入写入)");
//    	}
//        
//    	String sex = null;
//        if(person.getPersonSex().equals("0")){
//        	sex ="男";
//        }else{
//        	sex ="女";
//        }
//        log.info("转换 (性别："+person.getPersonSex()+") 为 (" + sex + ")");
//        final TMPPerson transformedPerson = new TMPPerson(person.getPersonName(), person.getPersonAge(),sex);
//        log.info("转换 (" + person + ") 为 (" + transformedPerson + ")");
// 
//        return transformedPerson;
//    }
//
//}
