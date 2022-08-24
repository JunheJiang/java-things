//package com.ljp.spring.batchtest;
//
//import org.springframework.batch.item.ItemReader;
//import org.springframework.batch.item.NonTransientResourceException;
//import org.springframework.batch.item.ParseException;
//import org.springframework.batch.item.UnexpectedInputException;
//import org.springframework.batch.item.adapter.AbstractMethodInvokingDelegator;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.ljp.spring.batchtest.service.PersonService;
//
//public class TMPNewItemReader<T> implements ItemReader<T> {
//	
//	
//	private PersonService personService;
//
//	public PersonService getPersonService() {
//		return personService;
//	}
//
//
//	public void setPersonService(PersonService personService) {
//		this.personService = personService;
//	}
//
//
//	@Override
//	public T read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
//		System.out.println("New itemreader。。。。。。。。。。。。。。");
//		Person person = personService.pullOnePerson();                                                                                                                                                                                                                                                                                           
//		return (T) person;
//		
//	}
//
//}
