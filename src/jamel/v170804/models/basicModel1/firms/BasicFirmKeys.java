package jamel.v170804.models.basicModel1.firms;

import jamel.v170804.data.AbstractDataKeys;

/**
 * Data keys of the {@code BasicFirm}.
 */
final class BasicFirmKeys extends AbstractDataKeys {

	@SuppressWarnings("javadoc")
	final public int assets = this.getNextIndex();

	@SuppressWarnings("javadoc")
	final public int capacity = this.getNextIndex();

	@SuppressWarnings("javadoc")
	final public int changePrice = this.getNextIndex();

	@SuppressWarnings("javadoc")
	final public int count = this.getNextIndex();

	@SuppressWarnings("javadoc")
	final public int deltaMarkup = this.getNextIndex();

	@SuppressWarnings("javadoc")
	final public int firing = this.getNextIndex();

	@SuppressWarnings("javadoc")
	final public int inventoriesNormalVolume = this.getNextIndex();

	@SuppressWarnings("javadoc")
	final public int inventoriesValue = this.getNextIndex();

	@SuppressWarnings("javadoc")
	final public int inventoriesVolume = this.getNextIndex();

	@SuppressWarnings("javadoc")
	final public int jobOffers = this.getNextIndex();

	@SuppressWarnings("javadoc")
	final public int liabilities = this.getNextIndex();

	@SuppressWarnings("javadoc")
	final public int markup = this.getNextIndex();

	@SuppressWarnings("javadoc")
	final public int money = this.getNextIndex();

	@SuppressWarnings("javadoc")
	final public int price = this.getNextIndex();

	@SuppressWarnings("javadoc")
	final public int productionValue = this.getNextIndex();

	@SuppressWarnings("javadoc")
	final public int productionVolume = this.getNextIndex();

	@SuppressWarnings("javadoc")
	final public int salesCosts = this.getNextIndex();

	@SuppressWarnings("javadoc")
	final public int salesValue = this.getNextIndex();

	@SuppressWarnings("javadoc")
	final public int salesVolume = this.getNextIndex();

	@SuppressWarnings("javadoc")
	final public int supplyCost = this.getNextIndex();

	@SuppressWarnings("javadoc")
	final public int supplyValue = this.getNextIndex();

	@SuppressWarnings("javadoc")
	final public int supplyVolume = this.getNextIndex();

	@SuppressWarnings("javadoc")
	final public int tangibleAssets = this.getNextIndex();

	@SuppressWarnings("javadoc")
	final public int vacancies = this.getNextIndex();

	@SuppressWarnings("javadoc")
	final public int wage = this.getNextIndex();

	@SuppressWarnings("javadoc")
	final public int workforce = this.getNextIndex();

	@SuppressWarnings("javadoc")
	final public int workforceTarget = this.getNextIndex();

	/**
	 * Creates a new set of data keys.
	 */
	BasicFirmKeys() {
		this.init(this.getClass().getFields());
	}

}