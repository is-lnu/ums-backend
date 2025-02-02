package org.lnu.is.web.rest.controller.department;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.lnu.is.facade.facade.department.DepartmentFacade;
import org.lnu.is.pagination.OrderBy;
import org.lnu.is.resource.department.DepartmentResource;
import org.lnu.is.resource.message.MessageResource;
import org.lnu.is.resource.message.MessageType;
import org.lnu.is.resource.search.PagedRequest;
import org.lnu.is.resource.search.PagedResultResource;
import org.lnu.is.web.rest.controller.AbstractControllerTest;
import org.lnu.is.web.rest.controller.BaseController;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;

import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class DepartmentControllerTest extends AbstractControllerTest {

	@Mock
	private DepartmentFacade facade;
	
	@InjectMocks
	private DepartmentController unit;
	
	@Override
	protected BaseController getUnit() {
		return unit;
	}
    
    @Test
	public void testCreateDepartment() throws Exception {
		// Given
    	String name = "fsd department";
    	String abbrName = "fds";
    	String manager = "manager";
    	
    	DepartmentResource departmentResource = new DepartmentResource();
    	departmentResource.setBegDate(new Date());
    	departmentResource.setEndDate(new Date());
		departmentResource.setAbbrName(abbrName );
		departmentResource.setName(name);
		departmentResource.setManager(manager);
		
		// When
    	String request = getJson(departmentResource, true);
		String response = getJson(departmentResource, false);
    	
		when(facade.createResource(any(DepartmentResource.class))).thenReturn(departmentResource);
		
    	// Then
		mockMvc.perform(post("/departments")
				.contentType(MediaType.APPLICATION_JSON)
				.content(request))
				.andExpect(status().isCreated())
				.andExpect(content().string(response));
		
		verify(facade).createResource(departmentResource);
	}
    
    @Test
	public void testUpdateDepartment() throws Exception {
		// Given
    	Long id = 1L;
    	String name = "fsd department";
    	String abbrName = "fds";
    	String manager = "manager";
    	
    	DepartmentResource departmentResource = new DepartmentResource();
    	departmentResource.setId(id);
    	departmentResource.setBegDate(new Date());
    	departmentResource.setEndDate(new Date());
		departmentResource.setAbbrName(abbrName );
		departmentResource.setName(name);
		departmentResource.setManager(manager);
		
		MessageResource responseResource = new MessageResource(MessageType.INFO, "Department Updated");
		
		// When
    	String request = getJson(departmentResource, true);
		String response = getJson(responseResource, false);
    	
		
    	// Then
		mockMvc.perform(put("/departments/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.content(request))
				.andExpect(status().isOk())
				.andExpect(content().string(response));
		
		verify(facade).updateResource(id, departmentResource);
	}
    
    @Test
	public void testGetSpecoffer() throws Exception {
		// Given
    	Long id = 1L;
    	String name = "fsd department";
    	String abbrName = "fds";
    	String manager = "manager";
    	
    	DepartmentResource departmentResource = new DepartmentResource();
    	departmentResource.setId(id);
    	departmentResource.setBegDate(new Date());
    	departmentResource.setEndDate(new Date());
		departmentResource.setAbbrName(abbrName );
		departmentResource.setName(name);
		departmentResource.setManager(manager);
		
		// When
		String response = getJson(departmentResource, false);
		
		when(facade.getResource(anyLong())).thenReturn(departmentResource);
		
		// Then
    	mockMvc.perform(get("/departments/{id}", id))
    		.andExpect(status().isOk())
    		.andExpect(content().string(response));
    	
    	verify(facade).getResource(id);
	}
    
    @Test
	public void testDeleteDepartment() throws Exception {
		// Given
    	Long id = 1L;
    	
		// When

		// Then
    	mockMvc.perform(delete("/departments/{id}", id))
    		.andExpect(status().is(204));
    	
    	verify(facade).removeResource(id);
	}
    
    @Test
	public void testGetDepartments() throws Exception {
		// Given
    	Long id = 1L;
    	String name = "fsd department";
    	String abbrName = "fds";
    	String manager = "manager";
    	
    	DepartmentResource departmentResource = new DepartmentResource();
    	departmentResource.setId(id);
    	departmentResource.setBegDate(new Date());
    	departmentResource.setEndDate(new Date());
		departmentResource.setAbbrName(abbrName );
		departmentResource.setName(name);
		departmentResource.setManager(manager);
		
    	long count = 100;
    	int limit = 25;
    	Integer offset = 10;
    	String uri = "/departments";
		List<DepartmentResource> entities = Arrays.asList(departmentResource);
    	PagedResultResource<DepartmentResource> expectedResource = new PagedResultResource<>();
		expectedResource.setCount(count);
		expectedResource.setLimit(limit);
		expectedResource.setOffset(offset);
		expectedResource.setUri(uri);
		expectedResource.setResources(entities);
		
		PagedRequest<DepartmentResource> pagedRequest = new PagedRequest<DepartmentResource>(new DepartmentResource(), offset, limit, Collections.<OrderBy>emptyList());
		
		// When
		when(facade.getResources(Matchers.<PagedRequest<DepartmentResource>>any())).thenReturn(expectedResource);
    	String response = getJson(expectedResource, false);

		// Then
    	mockMvc.perform(get("/departments")
    			.param("offset", String.valueOf(offset))
    			.param("limit", String.valueOf(limit)))
    		.andExpect(status().isOk())
    		.andExpect(content().string(response));
    	
		verify(facade).getResources(pagedRequest);
	}

	@Test(expected = AccessDeniedException.class)
	public void testGetResourceWithAccessDeniedException() throws Exception {
		// Given
		Long id = 1L;
		
		// When
		doThrow(AccessDeniedException.class).when(facade).getResource(anyLong());
		
		// Then
		mockMvc.perform(get("/departments/{id}", id));
		
		verify(facade).getResource(id);
	}
}
