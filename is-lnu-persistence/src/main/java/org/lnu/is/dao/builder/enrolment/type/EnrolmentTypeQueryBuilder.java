package org.lnu.is.dao.builder.enrolment.type;

import org.lnu.is.dao.annotations.QBuilder;
import org.lnu.is.dao.builder.AbstractQueryBuilder;
import org.lnu.is.dao.builder.BaseQueryBuilder;
import org.lnu.is.domain.enrolment.type.EnrolmentType;

/**
 * Enrolment Type Query Builder.
 * @author kushnir
 *
 */
@QBuilder("enrolmentTypeQueryBuilder")
public class EnrolmentTypeQueryBuilder extends AbstractQueryBuilder<EnrolmentType> {
	private static final String NAME_CONDITION = "e.name LIKE CONCAT('%',:name,'%') ";
	private static final String ABBRNAME_CONDITION = "e.abbrName LIKE CONCAT('%',:abbrName,'%') ";
	private static final String PARENT_CONDITION = "e.parent = :parent ";
	
	@Override
	protected String getBaseQuery() {
		return "SELECT e FROM EnrolmentType e %s";
	}

	@Override
	protected BaseQueryBuilder build(final EnrolmentType context, final BaseQueryBuilder builder) {
		return builder
				.where()
				.openBracket()
					.addAndCondition(PARENT_CONDITION, context.getParent())
					.addAndCondition(NAME_CONDITION, context.getName())
					.addAndCondition(ABBRNAME_CONDITION, context.getAbbrName())
				.closeBracket();
	}

}
