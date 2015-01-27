package gov.gsa.fssi.files.sourceFiles.utils.strategies.constraintValidations;

import gov.gsa.fssi.files.schemas.schemaFields.SchemaField;
import gov.gsa.fssi.files.schemas.schemaFields.fieldConstraints.FieldConstraint;
import gov.gsa.fssi.files.sourceFiles.records.datas.Data;
import gov.gsa.fssi.files.sourceFiles.utils.strategies.ConstraintValidationStrategy;

public class MaxLengthConstraintValidationStrategy implements ConstraintValidationStrategy {

	@Override
	public void validate(SchemaField field, FieldConstraint constraint, Data data) {
		if(data.getData().length() > Integer.parseInt(constraint.getValue())) data.setStatus(constraint.getLevel());
	}

	@Override
	public boolean isValid(SchemaField field, FieldConstraint constraint,
			Data data) {
		// TODO Auto-generated method stub
		return false;
	}

}