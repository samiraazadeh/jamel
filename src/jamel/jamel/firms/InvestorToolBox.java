package jamel.jamel.firms;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import jamel.Jamel;
import jamel.basic.util.Timer;
import jamel.jamel.firms.factory.BasicMachine;
import jamel.jamel.firms.factory.Machine;
import jamel.jamel.roles.Supplier;
import jamel.jamel.widgets.Cheque;
import jamel.jamel.widgets.Commodities;
import jamel.jamel.widgets.Supply;

/**
 * The investor tool box. TODO: WORK IN PROGRESS 19-09-2015
 */
public class InvestorToolBox {

	/**
	 * For debugging purpose.
	 */
	private static StringBuilder stringBuilder;

	/**
	 * Line separator.
	 */
	final private static String rc = System.getProperty("line.separator");

	/**
	 * Returns the present value of the specified income stream.
	 * 
	 * @param cashFlow
	 *            the income stream at each period.
	 * @param rate
	 *            the discount rate.
	 * @param forecastPeriod
	 *            the number of periods to be considered.
	 * @return the present value of the specified income stream.
	 */
	private static double getPresentValue(double cashFlow, float rate, int forecastPeriod) {
		final double coefficient;
		if (rate == 0) {
			coefficient = forecastPeriod;
		} else {
			final double rate2 = Math.pow(1 + rate, forecastPeriod);
			coefficient = (rate2 - 1) / (rate * rate2);
		}
		final double presentValue = coefficient * cashFlow;
		return presentValue;
	}

	/**
	 * Returns the expected profit of the specified investment project.
	 * 
	 * @param machines
	 *            the size of the investment (the number of machines to buy).
	 * @param initialOutlay
	 *            the initial outlay (i.e. the price of the new machines).
	 * @param productivity
	 *            the average productivity of the investment (= the average
	 *            productivity of one machine).
	 * @param machinery
	 *            an array that contains the productivity of each existing
	 *            machines, sorted in descending order.
	 * @param demandForecast
	 *            the volume of final goods to be produced by period.
	 * @param productPrice
	 *            the expected price of one unit of product.
	 * @param wage
	 *            the expected wage.
	 * @param rate
	 *            the rate of interest.
	 * @param forecastPeriod
	 *            the number of periods to consider in evaluating the project.
	 * @return the expected profit.
	 */
	private static double getPresentValue(final int machines, final long initialOutlay, final long productivity,
			final long[] machinery, double demandForecast, final double productPrice, final double wage,
			final float rate, final int forecastPeriod) {

		double effectiveProduction = 0;
		double wagebill = 0;

		// On suppose que les machines dont l'achat est projeté sont plus
		// productives que les anciennes.
		// Donc ce sont elles qui seront utilisées en priorité.
		for (int i = 0; i < machines; i++) {
			if (effectiveProduction + productivity <= demandForecast) {
				effectiveProduction += productivity;
				wagebill += wage;
			} else {
				wagebill += (wage * (demandForecast - effectiveProduction)) / productivity;
				effectiveProduction = demandForecast;
				break;
			}
		}

		// Si la production des nouvelles machines est insuffisante pour faire
		// face à la demande anticipée, on fait appel aux machines existantes,
		// en commençant par les plus productives (elles devraient être rangées
		// par productivité décroissante).
		if (effectiveProduction < demandForecast) {
			for (int i = 0; i < machinery.length; i++) {
				if (effectiveProduction + machinery[i] <= demandForecast) {
					effectiveProduction += machinery[i];
					wagebill += wage;
				} else {
					wagebill += (wage * (demandForecast - effectiveProduction)) / machinery[i];
					effectiveProduction = demandForecast;
					break;
				}
			}
		}

		final double sales = effectiveProduction * productPrice;
		final double operatingSurplus = sales - wagebill;

		final double presentValue = getPresentValue(operatingSurplus, rate, forecastPeriod) - initialOutlay;

		return presentValue;
	}

	/**
	 * Creates and returns the specified number of new machines.
	 * 
	 * @param n
	 *            the number of machines to be created.
	 * @param stuff
	 *            a heap of commodities that will serve as input for the
	 *            creation of the new machines.
	 * @param technology
	 *            the technology that will be used to create the new machines.
	 * @param timer
	 *            the timer.
	 * @param random
	 *            the random.
	 * @return the specified number of new machines.
	 */
	public static Machine[] getNewMachines(int n, Commodities stuff, Technology technology, Timer timer,
			Random random) {
		final Machine[] machines = new Machine[n];
		for (int i = 0; i < n; i++) {
			final Commodities input = stuff.detach(technology.getInputVolumeForANewMachine());
			machines[i] = new BasicMachine(technology, input, timer, random);
		}
		return machines;
	}

	/**
	 * Returns the optimum size of the investment.
	 * 
	 * @param machinePrices
	 *            an array that contains the prices of the new machines.
	 * @param productivity
	 *            the average productivity of the investment (= the average
	 *            productivity of one machine).
	 * @param machinery
	 *            an array that contains the productivity of each existing
	 *            machines, sorted in descending order.
	 * @param demandForecast
	 *            the volume of final goods to be produced by period.
	 * @param productPrice
	 *            the expected price of one unit of product.
	 * @param wage
	 *            the expected wage.
	 * @param rate
	 *            the rate of interest.
	 * @param forecastPeriod
	 *            the number of periods to consider in evaluating the project.
	 * @return the expected profit.
	 */
	public static int getOptimumSize(final Long[] machinePrices, final long productivity, final long[] machinery,
			final double demandForecast, final double productPrice, final double wage, final float rate,
			final int forecastPeriod) {

		double presentValue = getPresentValue(0, 0, productivity, machinery, demandForecast, productPrice, wage, rate,
				forecastPeriod);
		int investmentSize = 0;

		while (true) {
			final int targetedInvestmentSize = investmentSize + 1;
			if (targetedInvestmentSize == machinePrices.length) {
				// FIXME: il faut mesurer ce phénomène pour évaluer son
				// importance.
				//Jamel.println();
				//Jamel.println("InvestorToolBox.getOptimumSize(): Not enough sellers: " + machinePrices.length);
				//Jamel.println();
				break;
			}
			final double presentValue2 = getPresentValue(targetedInvestmentSize, machinePrices[targetedInvestmentSize],
					productivity, machinery, demandForecast, productPrice, wage, rate, forecastPeriod);
			if (presentValue2 > presentValue) {
				presentValue = presentValue2;
				investmentSize = targetedInvestmentSize;
			} else {
				break;
			}
		}

		if (investmentSize == 0) {
			// TODO: WORK IN PROGRESS 2015-09-30
			// On essaye de mettre des machines à la casse.

			//stringBuilder = new StringBuilder();
			//stringBuilder.append("Try to desinvest."+rc);

			final int realScrapValue = 100;
			// TODO realScrapValue should be an argument.
			int desinvest = 0;
			while (true) {
				desinvest++;
				if (desinvest>machinery.length) {
					break;
				}
				//stringBuilder.append("desinvest: "+desinvest+rc);
				final long scrapValue = (long) (desinvest * realScrapValue * productPrice);
				double effectiveProduction = 0;
				double wagebill = 0;
				for (int i = 0; i < machinery.length - desinvest; i++) {
					if (effectiveProduction + machinery[i] <= demandForecast) {
						effectiveProduction += machinery[i];
						wagebill += wage;
					} else {
						wagebill += (wage * (demandForecast - effectiveProduction)) / machinery[i];
						effectiveProduction = demandForecast;
						break;
					}
				}
				final double sales = effectiveProduction * productPrice;
				final double operatingSurplus = sales - wagebill;
				final double presentValue2 = getPresentValue(operatingSurplus, rate, forecastPeriod) + scrapValue;
				if (presentValue2 > presentValue) {
					// Le désinvestissement est rentable. On l'enregistre, et on
					// essaye un désinvestissement plus important.
					presentValue = presentValue2;
					investmentSize = -desinvest;
				} else {
					// Le désinvestissement n'est pas rentable, on abandonne.
					break;
				}
			}
			//Jamel.println(stringBuilder.toString());
		}

		return investmentSize;

	}

	/**
	 * Returns an array of that contains the prices of the new machines.
	 * <code>result[1]</code> contains the price of one machine,
	 * <code>result[2]</code> contains the price of two machines, etc.
	 * 
	 * @param supplies
	 *            the list of supplies of 'raw materials'.
	 * @param realCost
	 *            the volume of 'raw materials' required for building a new
	 *            machine.
	 * @return an array that contains the prices of the new machines.
	 */
	public static Long[] getPrices(Supply[] supplies, long realCost) {

		final List<Long> priceList = new ArrayList<Long>();

		long totalPrice = 0l;

		priceList.add(totalPrice);

		long incomplete = 0;
		for (int i = 0; i < supplies.length; i++) {
			if (supplies[i]!=null) {
				long remainingVolume = supplies[i].getVolume();
				final double price = supplies[i].getPrice();
				if (incomplete != 0) {
					final long need = realCost - incomplete;
					if (need <= remainingVolume) {
						totalPrice += need * price;
						priceList.add(totalPrice);
						remainingVolume -= need;
						incomplete = 0;
					} else {
						totalPrice += remainingVolume * price;
						incomplete += remainingVolume;
						remainingVolume = 0l;
					}
				}

				if (remainingVolume > 0) {
					if (remainingVolume >= realCost) {
						final int n = (int) (remainingVolume / realCost);
						for (int j = 0; j < n; j++) {
							totalPrice += realCost * price;
							priceList.add(totalPrice);
							remainingVolume -= realCost;
						}
					}

					if (remainingVolume > 0) {
						totalPrice += remainingVolume * price;
						incomplete += remainingVolume;
						remainingVolume = 0l;
					}

				}
			}			
			
		}

		final Long[] prices = priceList.toArray(new Long[priceList.size()]);

		return prices;
	}

	/**
	 * Performs some tests.
	 * 
	 * @param args
	 *            unused.
	 */
	public static void main(String[] args) {

		Jamel.println(Long.MAX_VALUE);

		class MySupply implements Supply {
			final private double myPrice;
			final private long myVolume;

			MySupply(double price, long volume) {
				this.myPrice = price;
				this.myVolume = volume;
			}

			@Override
			public Commodities buy(long volume, Cheque cheque) {
				throw new RuntimeException("Not implemented.");
			}

			@Override
			public double getPrice() {
				return this.myPrice;
			}

			@Override
			public long getPrice(long volume) {
				throw new RuntimeException("Not implemented.");
			}

			@Override
			public Supplier getSupplier() {
				throw new RuntimeException("Not implemented.");
			}

			@Override
			public long getVolume() {
				return this.myVolume;
			}
		}
		final Supply[] truc = new Supply[3];
		truc[0] = new MySupply(10, 3001);
		truc[1] = new MySupply(11, 3001);
		truc[2] = new MySupply(12, 3001);
		final Long[] prices = getPrices(truc, 1000);
		for (int i = 0; i < prices.length; i++) {
			Jamel.println(i + " machines = " + prices[i] + "; ");
			if (i != 0) {
				Jamel.println(prices[i] / i + " for one machine.");
			} else {
				Jamel.println();
			}
		}
	}

}

// ***
