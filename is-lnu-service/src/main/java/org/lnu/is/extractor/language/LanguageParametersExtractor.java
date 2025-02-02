package org.lnu.is.extractor.language;

import java.util.Map;

import org.lnu.is.annotations.ParametersExtractor;
import org.lnu.is.domain.language.Language;
import org.lnu.is.extractor.AbstractParametersExtractor;

/**
 * Language parameters extractor.
 * @author ivanursul
 *
 */
@ParametersExtractor("languageParametersExtractor")
public class LanguageParametersExtractor extends AbstractParametersExtractor<Language> {

	@Override
	public Map<String, Object> getParameters(final Language entity, final Map<String, Object> parameters) {
		
		addParameter(entity.getAbbrName(), "abbrName", parameters);
		addParameter(entity.getName(), "name", parameters);
		
		return parameters;
	}

}
