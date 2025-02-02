package org.lnu.is.extractor.department.address;

import java.util.Map;

import javax.annotation.Resource;

import org.lnu.is.annotations.ParametersExtractor;
import org.lnu.is.dao.dao.Dao;
import org.lnu.is.domain.address.type.AddressType;
import org.lnu.is.domain.admin.unit.AdminUnit;
import org.lnu.is.domain.department.Department;
import org.lnu.is.domain.department.address.DepartmentAddress;
import org.lnu.is.domain.street.type.StreetType;
import org.lnu.is.extractor.AbstractParametersExtractor;

/**
 * Department Address Parameters Extractor.
 * @author ivanursul
 *
 */
@ParametersExtractor("departmentAddressParametersExtractor")
public class DepartmentAddressParametersExtractor extends AbstractParametersExtractor<DepartmentAddress> {

	@Resource(name = "departmentDao")
	private Dao<Department, Department, Long> departmentDao;
	
	@Resource(name = "addressTypeDao")
	private Dao<AddressType, AddressType, Long> addressTypeDao;
	
	@Resource(name = "adminUnitDao")
	private Dao<AdminUnit, AdminUnit, Long> adminUnitDao;
	
	@Resource(name = "streetTypeDao")
	private Dao<StreetType, StreetType, Long> streetTypeDao;
	
	@Override
	protected Map<String, Object> getParameters(final DepartmentAddress entity, final Map<String, Object> parameters) {
		
		addParameter(entity.getDepartment(), departmentDao, "department", parameters);
		addParameter(entity.getAddressType(), addressTypeDao, "addressType", parameters);
		addParameter(entity.getAdminUnit(), adminUnitDao, "adminUnit", parameters);
		addParameter(entity.getStreetType(), streetTypeDao, "streetType", parameters);

		addParameter(entity.getZipCode(), "zipCode", parameters);
		addParameter(entity.getStreet(), "street", parameters);
		addParameter(entity.getHouse(), "house", parameters);
		addParameter(entity.getApartment(), "apartment", parameters);
		
		return parameters;
	}

}
