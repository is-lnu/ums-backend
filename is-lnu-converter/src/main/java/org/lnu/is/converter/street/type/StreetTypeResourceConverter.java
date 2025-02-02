package org.lnu.is.converter.street.type;

import org.lnu.is.annotations.Converter;
import org.lnu.is.converter.AbstractConverter;
import org.lnu.is.domain.street.type.StreetType;
import org.lnu.is.resource.street.type.StreetTypeResource;

/**
 * StreetType resource converter.
 * @author ivanursul
 *
 */
@Converter("streetTypeResourceConverter")
public class StreetTypeResourceConverter extends AbstractConverter<StreetTypeResource, StreetType> {

	@Override
	public StreetType convert(final StreetTypeResource source, final StreetType target) {
		
		target.setName(source.getName());
		target.setAbbrName(source.getAbbrName());

		return target;
	}

	@Override
	public StreetType convert(final StreetTypeResource source) {
		return convert(source, new StreetType());
	}

}
