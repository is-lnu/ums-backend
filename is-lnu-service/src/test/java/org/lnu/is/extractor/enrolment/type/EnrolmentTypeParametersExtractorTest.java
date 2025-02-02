package org.lnu.is.extractor.enrolment.type;

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
import org.lnu.is.domain.enrolment.type.EnrolmentType;
import org.lnu.is.security.service.SessionService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EnrolmentTypeParametersExtractorTest {

	@Mock
	private Dao<EnrolmentType, EnrolmentType, Long> enrolmentTypeDao;
	
	@InjectMocks
	private EnrolmentTypeParametersExtractor unit;

	@Mock
	private SessionService sessionService;

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
		Long parentId = 1L;
		EnrolmentType parent = new EnrolmentType();
		parent.setId(parentId);
		
		String name = "AddressN";
		String abbrname = "fsdfds";
		
		EnrolmentType entity = new EnrolmentType();
		entity.setName(name);
		entity.setAbbrName(abbrname);
		entity.setParent(parent);
		
		
		Map<String, Object> expected = new HashMap<String, Object>();
		expected.put("parent", parent);
		expected.put("name", name);
		expected.put("abbrName", abbrname);
		expected.put("status", RowStatus.ACTIVE);
		expected.put("userGroups", groups);
		
		// When
		when(enrolmentTypeDao.getEntityById(anyLong())).thenReturn(parent);
		Map<String, Object> actual = unit.getParameters(entity);
		
		// Then
		verify(enrolmentTypeDao).getEntityById(parentId);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetParametersWithDisabledDefaults() throws Exception {
		// Given
		unit.setActive(false);
		unit.setSecurity(false);
		
		String name = "AddressN";
		String abbrname = "fsdfds";
		
		EnrolmentType entity = new EnrolmentType();
		entity.setName(name);
		entity.setAbbrName(abbrname);
		
		Map<String, Object> expected = new HashMap<String, Object>();
		expected.put("name", name);
		expected.put("abbrName", abbrname);
		
		// When
		Map<String, Object> actual = unit.getParameters(entity);
		
		// Then
		assertEquals(expected, actual);
	}

	@Test
	public void testGetParametersWithNoRelations() throws Exception {
		// Given
		EnrolmentType entity = new EnrolmentType();

		Map<String, Object> expected = new HashMap<String, Object>();
		expected.put("status", RowStatus.ACTIVE);
		expected.put("userGroups", groups);
		
		// When
		Map<String, Object> actual = unit.getParameters(entity);

		// Then
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetParametersWithNoRelationsAndDisabledSecurity() throws Exception {
		// Given
		unit.setSecurity(false);
		
		EnrolmentType entity = new EnrolmentType();
		
		Map<String, Object> expected = new HashMap<String, Object>();
		expected.put("status", RowStatus.ACTIVE);
		
		// When
		Map<String, Object> actual = unit.getParameters(entity);
		
		// Then
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetParametersWithNoRelationsAndDisabledStatus() throws Exception {
		// Given
		unit.setActive(false);
		EnrolmentType entity = new EnrolmentType();
		
		Map<String, Object> expected = new HashMap<String, Object>();
		expected.put("userGroups", groups);
		
		// When
		Map<String, Object> actual = unit.getParameters(entity);
		
		// Then
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetParametersWithNoRelationsAndDisabledDefaults() throws Exception {
		// Given
		unit.setActive(false);
		unit.setSecurity(false);
		
		EnrolmentType entity = new EnrolmentType();
		
		Map<String, Object> expected = new HashMap<String, Object>();
		
		// When
		Map<String, Object> actual = unit.getParameters(entity);
		
		// Then
		assertEquals(expected, actual);
	}
}
