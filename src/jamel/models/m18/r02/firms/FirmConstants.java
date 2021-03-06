package jamel.models.m18.r02.firms;

import jamel.util.Parameters;

/**
 * A class to parse and store the constant parameters of the firm.
 */
@SuppressWarnings("javadoc")
class FirmConstants {

	final String banks;

	final int initialCapacity;

	final float initialMarkupMax;

	final float initialMarkupMin;

	final float initialUtilizationRate;

	final float inventoryNormalLevel;

	final int jobContractMax;

	final int jobContractMin;

	final String laborMarket;

	final int longTerm;

	final float mutation;

	final int nSupplier = 10; // TODO Should be a parameter.

	final int observations;

	final float productionAdjustment = 6; // TODO Should be a
											// parameter.

	final float retentionRate = 0.5f; // TODO Should be a parameter.

	final String shareholders;

	final int shortTerm;

	final int supervision;

	final String suppliers;

	final float wageFlexibility;

	final double wageInitialValue;

	/**
	 * Creates a new set of parameters by parsing the specified
	 * {@code Parameters}.
	 * 
	 * @param params
	 *            the parameters to be parsed.
	 */
	FirmConstants(Parameters params) {
		this.supervision = params.getInt("supervision");
		this.jobContractMax = params.getInt("workforce.jobContracts.max");
		this.jobContractMin = params.getInt("workforce.jobContracts.min");

		this.initialMarkupMin = params.getFloat("initial.markup.min");
		this.initialMarkupMax = params.getFloat("initial.markup.max");

		this.observations = params.getInt("wage.observations").intValue();
		this.wageFlexibility = params.getFloat("wage.flexibility");
		this.wageInitialValue = params.getDoubleValue("wage.initialValue");

		this.shortTerm = params.getInt("credit.term.short");
		this.longTerm = params.getInt("credit.term.long");
		this.inventoryNormalLevel = params.getFloat("inventories.normalLevel");
		this.mutation = params.getFloat("mutation.strenght").floatValue();
		this.initialCapacity = params.getInt("production.capacity").intValue();
		this.initialUtilizationRate = params.getFloat("utilizationRate.initialValue");
		this.shareholders = params.getString("shareholders");
		this.banks = params.getString("bankSector");
		this.laborMarket = params.getString("laborMarket");
		this.suppliers = params.getString("production.machines.creation.input.suppliers");
	}
}