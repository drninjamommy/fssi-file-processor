package test.java.gov.gsa.fssi.files.sourceFiles.utils.strategies.constraintValidations;

import main.java.gov.gsa.fssi.files.schemas.schemaFields.SchemaField;
import main.java.gov.gsa.fssi.files.schemas.schemaFields.fieldConstraints.FieldConstraint;
import main.java.gov.gsa.fssi.files.sourceFiles.records.datas.Data;
import main.java.gov.gsa.fssi.files.sourceFiles.utils.contexts.ConstraintValidationContext;
import main.java.gov.gsa.fssi.files.sourceFiles.utils.strategies.constraintValidations.RequiredConstraintValidationStrategy;

import org.junit.Assert;
import org.junit.Test;

import test.java.gov.gsa.fssi.mockData.MockData;
import test.java.gov.gsa.fssi.mockData.MockFieldConstraint;
import test.java.gov.gsa.fssi.mockData.MockSchemaField;

public class RequiredConstraintValidationStrategyTest {

	/**
	 * This should test to make sure that we are catching required Fields
	 */
	@Test
	public void testRequiredWhenNull() {
		ConstraintValidationContext context = new ConstraintValidationContext();
		context.setDataValidationStrategy(new RequiredConstraintValidationStrategy());
		
		FieldConstraint fieldConstraint = MockFieldConstraint.make(FieldConstraint.TYPE_REQUIRED, "true", 2);	
		SchemaField field = MockSchemaField.make("REQUIRED", SchemaField.TYPE_STRING, fieldConstraint);
		Data data = MockData.make();
		
		context.validate(field, fieldConstraint, data);
		//data.setStatus(2);
		Assert.assertEquals("failure - RequiredConstraintValidationStrategyTest did not catch error", fieldConstraint.getLevel(), data.getMaxErrorLevel());
		Assert.assertEquals("failure - RequiredConstraintValidationStrategyTest did not make failure", false, data.getStatus());		
	}

	
	/**
	 * This should test to make sure that we are catching required Fields
	 */
	@Test
	public void testRequiredWhenEmpty() {
		ConstraintValidationContext context = new ConstraintValidationContext();
		context.setDataValidationStrategy(new RequiredConstraintValidationStrategy());
		
		FieldConstraint fieldConstraint = MockFieldConstraint.make(FieldConstraint.TYPE_REQUIRED, "true", 2);	
		SchemaField field = MockSchemaField.make("REQUIRED", SchemaField.TYPE_STRING, fieldConstraint);
		Data data = MockData.make("");
		
		context.validate(field, fieldConstraint, data);
		//data.setStatus(2);
		Assert.assertEquals("failure - RequiredConstraintValidationStrategyTest did not catch error", fieldConstraint.getLevel(), data.getMaxErrorLevel());
		Assert.assertEquals("failure - RequiredConstraintValidationStrategyTest did not make failure", false, data.getStatus());			
	}	
	
	
	/**
	 * This should test to make sure that we are catching required Fields
	 */
	@Test
	public void testRequiredWhenPopulated() {
		ConstraintValidationContext context = new ConstraintValidationContext();
		context.setDataValidationStrategy(new RequiredConstraintValidationStrategy());
		
		FieldConstraint fieldConstraint = MockFieldConstraint.make(FieldConstraint.TYPE_REQUIRED, "true", 2);	
		SchemaField field = MockSchemaField.make("REQUIRED", SchemaField.TYPE_STRING, fieldConstraint);
		Data data = MockData.make("Value");
		
		context.validate(field, fieldConstraint, data);
		//data.setStatus(2);
		Assert.assertEquals("failure - RequiredConstraintValidationStrategyTest caughtError", 0, data.getMaxErrorLevel());
		Assert.assertEquals("failure - RequiredConstraintValidationStrategyTest did not pass", true, data.getStatus());				
	}	

}
