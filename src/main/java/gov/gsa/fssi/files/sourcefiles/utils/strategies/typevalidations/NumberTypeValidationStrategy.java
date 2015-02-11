package main.java.gov.gsa.fssi.files.sourcefiles.utils.strategies.typevalidations;

import main.java.gov.gsa.fssi.files.schemas.schemafields.SchemaField;
import main.java.gov.gsa.fssi.files.sourcefiles.records.datas.Data;
import main.java.gov.gsa.fssi.files.sourcefiles.utils.strategies.TypeValidationStrategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NumberTypeValidationStrategy implements TypeValidationStrategy {
	static final Logger logger = LoggerFactory
			.getLogger(NumberTypeValidationStrategy.class);
	@Override
	public void validate(SchemaField field, Data data) {
		if (data != null) {
			if (!data.getData().isEmpty() && !data.getData().equals("")) {

				Double number = null;
				try {
					number = Double.valueOf(data.getData());
				} catch (NumberFormatException e) {
					if (logger.isDebugEnabled())
						logger.debug(
								"Received error '{}' when trying to convert '{}' to Number",
								e.getMessage(), data.getData());

				}

				if (number == null)
					data.addValidationResult(false, 3,
							"Type(" + field.getType() + ")"); // Fatal
				else
					data.addValidationResult(true, 0, "Type(" + field.getType()
							+ ")");
			} else
				data.addValidationResult(true, 0, "Type(" + field.getType()
						+ ")");
		}
	}

	@Override
	public boolean isValid(SchemaField field, Data data) {
		// TODO Auto-generated method stub
		return false;
	}

}
