package main.java.gov.gsa.fssi.files.sourcefiles.utils.strategies.constraintvalidations;

import main.java.gov.gsa.fssi.files.schemas.schemafields.SchemaField;
import main.java.gov.gsa.fssi.files.schemas.schemafields.fieldconstraints.FieldConstraint;
import main.java.gov.gsa.fssi.files.sourcefiles.records.datas.Data;
import main.java.gov.gsa.fssi.files.sourcefiles.utils.strategies.ConstraintValidationStrategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequiredConstraintValidationStrategy implements
		ConstraintValidationStrategy {
	private static final Logger logger = LoggerFactory
			.getLogger(RequiredConstraintValidationStrategy.class);

	@Override
	public boolean isValid(SchemaField field, FieldConstraint constraint,
			Data data) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void validate(SchemaField field, FieldConstraint constraint,
			Data data) {
		if (data != null) {
			if ((data.getData() == null || data.getData().isEmpty())
					&& constraint.getValue().equalsIgnoreCase("TRUE")) {
				data.addValidationResult(false, constraint.getLevel(),
						constraint.getRuleText());
			} else
				data.addValidationResult(true, 0, constraint.getRuleText());
		}
	}

}
