package jamel.util;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * The expression factory.
 */
public class ExpressionFactory extends JamelObject {

	/**
	 * Returns a query cleaned from useless parentheses and spaces.
	 * 
	 * @param query
	 *            the query to be cleaned up.
	 * @return the cleaned up string.
	 */
	private static String cleanUp(final String query) {
		final String result;
		if (query.startsWith("+")) {
			final String str2 = query.substring(1, query.length());
			result = cleanUp(str2);
		} else if (query.charAt(0) == '(' && query.charAt(query.length() - 1) == ')') {
			int count = 1;
			for (int i = 1; i < query.length() - 1; i++) {
				if (query.charAt(i) == '(') {
					count++;
				} else if (query.charAt(i) == ')') {
					count--;
					if (count == 0) {
						break;
					}
				}
			}
			if (count == 1) {
				// Removes the global parentheses.
				final String str2 = query.substring(1, query.length() - 1);
				result = cleanUp(str2);
			} else {
				// Nothing to remove.
				result = query;
			}
		} else {
			// Nothing to remove.
			result = query;
		}
		return result.trim();
	}

	/**
	 * Returns the specified addition.
	 * 
	 * @param arg1
	 *            the augend.
	 * @param arg2
	 *            the addend.
	 * @return the specified addition.
	 */
	private static Expression getAddition(final Expression arg1, final Expression arg2) {
		ArgChecks.nullNotPermitted(arg1, "arg1");
		ArgChecks.nullNotPermitted(arg2, "arg2");
		final Expression result = new Expression() {

			@Override
			public Double getValue() {

				final Double value;
				if (arg1.getValue() == null || arg2.getValue() == null) {
					value = null;
				} else {
					value = arg1.getValue() + arg2.getValue();
				}
				return value;

			}

			@Override
			public String toString() {
				return "(" + arg1.toString() + " + " + arg2.toString() + ")";
			}

		};
		return result;
	}

	/**
	 * Returns the specified division.
	 * 
	 * @param arg1
	 *            the dividend.
	 * @param arg2
	 *            the divisor.
	 * @return the specified division.
	 */
	private static Expression getDivision(final Expression arg1, final Expression arg2) {
		ArgChecks.nullNotPermitted(arg1, "arg1");
		ArgChecks.nullNotPermitted(arg2, "arg2");
		final Expression result = new Expression() {

			@Override
			public Double getValue() {
				final Double value;
				if (arg1.getValue() == null || arg2.getValue() == null || arg2.getValue() == 0) {
					value = null;
				} else {
					value = arg1.getValue() / arg2.getValue();
				}
				return value;
			}

			@Override
			public String toString() {
				return arg1.toString() + " / " + arg2.toString();
			}

		};
		return result;
	}

	/**
	 * Returns the modulo.
	 * 
	 * @param arg1
	 *            the dividend.
	 * @param arg2
	 *            the divisor.
	 * @return the specified modulo operation.
	 */
	private static Expression getModulo(final Expression arg1, final Expression arg2) {
		ArgChecks.nullNotPermitted(arg1, "arg1");
		ArgChecks.nullNotPermitted(arg2, "arg2");
		final Expression result = new Expression() {

			@Override
			public Double getValue() {
				final Double value;
				if (arg1.getValue() == null || arg2.getValue() == null || arg2.getValue() == 0) {
					value = null;
				} else {
					value = arg1.getValue() % arg2.getValue();
				}
				return value;
			}

			@Override
			public String toString() {
				return arg1.toString() + " % " + arg2.toString();
			}

		};
		return result;
	}

	/**
	 * Returns the specified multiplication.
	 * 
	 * @param arg1
	 *            the first factor.
	 * @param arg2
	 *            the second factor.
	 * @return the specified multiplication.
	 */
	private static Expression getMultiplication(final Expression arg1, final Expression arg2) {
		ArgChecks.nullNotPermitted(arg1, "arg1");
		ArgChecks.nullNotPermitted(arg2, "arg2");
		final Expression result = new Expression() {

			@Override
			public Double getValue() {
				final Double value;
				if (arg1.getValue() == null || arg2.getValue() == null) {
					value = null;
				} else {
					value = arg1.getValue() * arg2.getValue();
				}
				return value;
			}

			@Override
			public String toString() {
				return arg1.toString() + " * " + arg2.toString();
			}

		};
		return result;
	}

	/**
	 * Returns a new "null" expression.
	 * 
	 * @return a new "null" expression.
	 */
	private static Expression getNull() {
		return new Expression() {

			@Override
			public Double getValue() {
				return null;
			}

			@Override
			public String toString() {
				return "null";
			}

		};
	}

	/**
	 * Returns an expression that represents the specified numeric constant.
	 * 
	 * @param d
	 *            the numeric constant.
	 * @return an expression that represents the specified numeric constant.
	 */
	private static Expression getNumeric(final double d) {

		final Expression result = new Expression() {

			@Override
			public Double getValue() {
				return d;
			}

			@Override
			public String toString() {
				return "" + d;
			}

		};
		return result;
	}

	/**
	 * Returns the opposite of the specified expression.
	 * 
	 * @param arg
	 *            the specified expression.
	 * @return the opposite of the specified expression.
	 */
	private static Expression getOpposite(final Expression arg) {
		ArgChecks.nullNotPermitted(arg, "arg");
		final Expression result = new Expression() {

			@Override
			public Double getValue() {
				return -arg.getValue();
			}

			@Override
			public String toString() {
				return "- " + arg.toString();
			}

		};
		return result;
	}

	/**
	 * Returns the specified subtraction.
	 * 
	 * @param arg1
	 *            the minuend.
	 * @param arg2
	 *            the subtrahend.
	 * @return the specified subtraction.
	 */
	private static Expression getSubtraction(final Expression arg1, final Expression arg2) {
		ArgChecks.nullNotPermitted(arg1, "arg1");
		ArgChecks.nullNotPermitted(arg2, "arg2");
		final Expression result = new Expression() {

			@Override
			public Double getValue() {

				final Double value;
				if (arg1.getValue() == null || arg2.getValue() == null) {
					value = null;
				} else {
					value = arg1.getValue() - arg2.getValue();
				}
				return value;

			}

			@Override
			public String toString() {
				return "(" + arg1.toString() + " - " + arg2.toString() + ")";
			}

		};
		return result;
	}

	/**
	 * Compares the value of the first expression against the value of the
	 * second expression. The result is 1 if and only if the arguments are not
	 * null and the double values are the same.
	 * 
	 * @param arg1
	 *            the first expression
	 * @param arg2
	 *            the second expression
	 * @return <code>1</code> if the values of the expressions are the same;
	 *         <code>0</code> otherwise.
	 */
	private static Expression getTestEqual(Expression arg1, Expression arg2) {
		ArgChecks.nullNotPermitted(arg1, "arg1");
		ArgChecks.nullNotPermitted(arg2, "arg2");
		final Expression result = new Expression() {

			@Override
			public Double getValue() {
				final Double value;
				if (arg1.getValue() == null || arg2.getValue() == null) {
					value = null;
				} else if (arg1.getValue().equals(arg2.getValue())) {
					value = 1.;
				} else {
					value = 0.;
				}
				return value;
			}

			@Override
			public String toString() {
				return "isEqual(" + arg1.toString() + ", " + arg2.toString() + ")";
			}

		};
		return result;
	}

	/**
	 * Returns <code>true</code> if parentheses in the specified query are
	 * balanced, <code>false</code> otherwise.
	 * 
	 * @param query
	 *            the query.
	 * @return <code>true</code> if parentheses in the specified query are
	 *         balanced, <code>false</code> otherwise.
	 */
	private static boolean isBalanced(String query) {
		int count = 0;
		for (int i = 0; i < query.length(); i++) {
			if (query.charAt(i) == '(') {
				count++;
			} else if (query.charAt(i) == ')') {
				count--;
				if (count < 0) {
					// Not balanced !
					return false;
				}
			}
		}
		return count == 0;
	}

	/**
	 * Splits this string in two substrings around the first comma.
	 * Commas within parentheses are ignored.
	 *
	 * @param input
	 *            the string to be split.
	 * @return the array of strings computed by splitting the given string
	 */
	private static String[] split2(final String input) {
		final String[] result;
		Integer position = null;
		int count = 0;
		for (int i = 0; i < input.length(); i++) {
			final char c = input.charAt(i);
			if (c == '(') {
				count++;
			} else if (c == ')') {
				count--;
				if (count < 0) {
					throw new RuntimeException("Parentheses not balanced: " + input);
				}
			} else if (count == 0) {
				// We are outside parentheses.
				// Is this char an comma ?
				if (c == ',') {
					position = i;
					break;
				}
			}
		}
		if (count != 0) {
			throw new RuntimeException("Parentheses not balanced: " + input);
		}
		if (position != null) {
			result = new String[2];
			result[0] = input.substring(0, position);
			result[1] = input.substring(position + 1);
		} else {
			result = new String[1];
			result[0] = input;
		}
		return result;
	}

	/**
	 * Returns a new constant expression.
	 * 
	 * @param arg1
	 *            the number value to be returned by the expression.
	 * @return a new constant expression.
	 */
	public static Expression getConstant(Number arg1) {
		ArgChecks.nullNotPermitted(arg1, "arg1");
		final Expression result;
		if (arg1 == null) {
			result = getNull();
		} else {
			result = getNumeric(arg1.doubleValue());
		}
		return result;
	}

	/**
	 * For debugging purposes.
	 * 
	 * @param args
	 *            not used.
	 */
	public static void main(String[] args) {
		/*final String[] split = split("a,truc(a,b,c),b");
		for (int i = 0; i < split.length; i++) {
			Jamel.println("[" + split[i] + "]");
		}*/
	}

	/**
	 * Splits the given input String around commas.
	 * Commas within parenthesis are ignored.
	 *
	 * @param input
	 *            the string to be split
	 * @return the array of strings computed by splitting the given string
	 */
	public static String[] split(final String input) {
		final ArrayList<String> list = new ArrayList<>();
		String string = input;
		while (true) {
			final String[] output = split2(string);
			list.add(output[0]);
			if (output.length == 1) {
				break;
			}
			string = output[1];
		}
		return list.toArray(new String[list.size()]);
	}

	/**
	 * Creates a new Expression factory for the specified simulation.
	 * 
	 * @param simulation
	 *            the parent simulation.
	 */
	public ExpressionFactory(Simulation simulation) {
		super(simulation);
	}

	/**
	 * Returns the specified expression.
	 * 
	 * @param query
	 *            a string that describes the expression to be returned.
	 * @return the specified expression.
	 */
	public Expression getExpression(final String query) {

		// Jamel.println("getExpression", "\'" + query + "\'");

		final Expression result;

		if (!isBalanced(query)) {
			throw new RuntimeException("Not balanced: " + query);
			// TODO Comment traiter cet incident ?
			// Il n'est pas dû à Jamel mais au scénario, il faut informer
			// clairement l'utilisateur de l'endroit où il s'est planté.
		}

		final String cleaned = cleanUp(query.replaceAll("(\\p{javaSpaceChar}|\\r|\\n|\\t)", ""));

		Character operator = null;
		Integer position = null;
		int count = 0;

		for (int i = 0; i < cleaned.length(); i++) {

			final char c = cleaned.charAt(i);

			if (c == '(') {
				count++;
			} else if (c == ')') {
				count--;
			}

			else if (count == 0 && i > 0) {

				// We are outside parentheses.
				// Is this char an operator ?

				if (c == '+') {
					operator = c;
					position = i;
					break;
				} else if (c == '-') {
					final char previous = cleaned.charAt(i - 1);
					if (previous != '*' && previous != '/') {
						operator = c;
						position = i;
						break;
					}
				} else if (c == '*' || c == '/' || c == '%') {
					operator = c;
					position = i;
				}
			}
		}

		if (position != null) {
			if (operator == null) {
				throw new RuntimeException("Operator is null");
			}
			final Expression arg1 = getExpression(cleaned.substring(0, position));
			final Expression arg2 = getExpression(cleaned.substring(position + 1));

			switch (operator) {
			case '+':
				result = getAddition(arg1, arg2);
				break;
			case '-':
				result = getSubtraction(arg1, arg2);
				break;
			case '*':
				result = getMultiplication(arg1, arg2);
				break;
			case '/':
				result = getDivision(arg1, arg2);
				break;
			case '%':
				result = getModulo(arg1, arg2);
				break;
			default:
				throw new RuntimeException("Unexpected operator: " + operator);
			}

		} else {
			if (cleaned.startsWith("-")) {
				result = getOpposite(getExpression(cleaned.substring(1)));
			} else if (Pattern.matches("\\d.*", cleaned)) {
				result = getNumeric(Double.parseDouble(cleaned));
			} else if (Pattern.matches("isEqual[\\(].*[\\)]", cleaned)) {
				final String argString = cleaned.substring(8, cleaned.length() - 1);
				final String[] args = split(argString);
				if (args.length != 2) {
					throw new RuntimeException("Bad number of parameters: " + cleaned);
				}
				result = getTestEqual(getExpression(args[0]), getExpression(args[1]));
			} else {
				result = this.getSimulation().getDataAccess(cleaned);
			}
		}

		return result;

	}

}