//package com.ljp.spring.batchtest;
//
//import java.util.List;
//
//import org.springframework.batch.item.ItemWriter;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.ljp.spring.batchtest.service.PersonService;
//
//
//@SuppressWarnings("hiding")
//public class TMPNewItemWriter implements ItemWriter<Person> {
//	@Autowired
//	PersonService personService;
//	
//	
//	@Override
//	public  void write(List<? extends Person> listPersons) throws Exception {
//		// TODO Auto-generated method stub
//		System.out.println("-------writer--write()-------List size:" + listPersons.size());
//		for(Person person : listPersons){
//			personService.saveOnePerson(person);
//			
//		}
//		
//	}
//
//	
//}
