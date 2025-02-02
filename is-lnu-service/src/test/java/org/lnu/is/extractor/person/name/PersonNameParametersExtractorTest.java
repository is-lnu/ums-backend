package org.lnu.is.extractor.person.name;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lnu.is.dao.dao.Dao;
import org.lnu.is.domain.common.RowStatus;
import org.lnu.is.domain.language.Language;
import org.lnu.is.domain.person.Person;
import org.lnu.is.domain.person.name.PersonName;
import org.lnu.is.security.service.SessionService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PersonNameParametersExtractorTest {

	@Mock
	private SessionService sessionService;
	
	@Mock
	private Dao<Language, Language, Long> languageDao;
	
	@Mock
	private Dao<Person, Person, Long> personDao;
	
	@InjectMocks
	private PersonNameParametersExtractor unit;

	private Boolean active = true;
	private Boolean security = true;

	private String group1 = "developers";
	private String group2 = "students";
	
	private List<String> groups = Arrays.asList(group1, group2);
	
	@Before
	public void setup() {
		unit.setActive(active);
		unit.setSecurity(security);
		
		when(sessionService.getGroups()).thenReturn(groups);
	}
	
	@Test
	public void testGetParameters() throws Exception {
		// Given
		Long personId = 1L;
		Person person = new Person();
		person.setId(personId);
		
		Long languageId = 2L;
		Language language = new Language();
		language.setId(languageId);

		String firstName = "first name";
		String fatherName = "father name";
		PersonName entity = new PersonName();
		entity.setPerson(person);
		entity.setLanguage(language);
		entity.setFirstName(firstName);
		entity.setFatherName(fatherName);

		Map<String, Object> expected = new HashMap<String, Object>();
		expected.put("person", person);
		expected.put("language", language);
		expected.put("firstName", firstName);
		expected.put("fatherName", fatherName);
		expected.put("status", RowStatus.ACTIVE);
		expected.put("userGroups", groups);
		
		// When
		when(languageDao.getEntityById(anyLong())).thenReturn(language);
		when(personDao.getEntityById(anyLong())).thenReturn(person);
		
		Map<String, Object> actual = unit.getParameters(entity);

		// Then
		verify(languageDao).getEntityById(languageId);
		verify(personDao).getEntityById(personId);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetParametersWithDisabledSecurity() throws Exception {
		// Given
		unit.setActive(false);
		unit.setSecurity(false);
		
		Long personId = 1L;
		Person person = new Person();
		person.setId(personId);
		
		Long languageId = 2L;
		Language language = new Language();
		language.setId(languageId);
		
		String firstName = "first name";
		String fatherName = "father name";
		PersonName entity = new PersonName();
		entity.setPerson(person);
		entity.setLanguage(language);
		entity.setFirstName(firstName);
		entity.setFatherName(fatherName);
		
		Map<String, Object> expected = new HashMap<String, Object>();
		expected.put("person", person);
		expected.put("language", language);
		expected.put("firstName", firstName);
		expected.put("fatherName", fatherName);
		
		// When
		when(languageDao.getEntityById(anyLong())).thenReturn(language);
		when(personDao.getEntityById(anyLong())).thenReturn(person);
		
		Map<String, Object> actual = unit.getParameters(entity);
		
		// Then
		verify(languageDao).getEntityById(languageId);
		verify(personDao).getEntityById(personId);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetParametersWithDefaultEntity() throws Exception {
		// Given
		PersonName entity = new PersonName();
		Map<String, Object> expected = new HashMap<String, Object>();
		expected.put("status", RowStatus.ACTIVE);
		expected.put("userGroups", groups);
		
		// When
		Map<String, Object> actual = unit.getParameters(entity);

		// Then
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetParametersWithDefaultEntityAndDisabledSecurity() throws Exception {
		// Given
		unit.setSecurity(false);
		
		PersonName entity = new PersonName();
		Map<String, Object> expected = new HashMap<String, Object>();
		expected.put("status", RowStatus.ACTIVE);
		
		// When
		Map<String, Object> actual = unit.getParameters(entity);
		
		// Then
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetParametersWithDefaultEntityAndDisabledStatus() throws Exception {
		// Given
		unit.setActive(false);
		
		PersonName entity = new PersonName();
		Map<String, Object> expected = new HashMap<String, Object>();
		expected.put("userGroups", groups);
		
		// When
		Map<String, Object> actual = unit.getParameters(entity);
		
		// Then
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetParametersWithDefaultEntityAndDisabledDefaults() throws Exception {
		// Given
		unit.setActive(false);
		unit.setSecurity(false);
		
		PersonName entity = new PersonName();
		Map<String, Object> expected = new HashMap<String, Object>();
		
		// When
		Map<String, Object> actual = unit.getParameters(entity);
		
		// Then
		assertEquals(expected, actual);
	}
}
