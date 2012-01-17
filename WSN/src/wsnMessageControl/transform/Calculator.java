package wsnMessageControl.transform;

import java.util.Stack;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class Calculator {
	private static Calculator self = new Calculator();

	public static Calculator getInstance() {
		return self;
	}

	private static Log logger = LogFactory.getLog(Calculator.class);

	public double infixCalc(String expression) {
		return postfixCalc(InfixToPostfix.convert(expression));

	}

	public double postfixCalc(String expression) {
		Stack<Double> stack = new Stack<Double>();
		for (int i = 0; i < expression.length();) {
			for (int j = i; j < expression.length(); j++) {
				if (expression.substring(j, j + 1).equals("+")) {
					double y = stack.pop();
					double x = stack.pop();
					stack.push((add(x, y)));
					i = j + 1;
					break;
				} else if (expression.substring(j, j + 1).equals("-")) {
					double y = stack.pop();
					double x = stack.pop();
					stack.push(subtract(x, y));
					i = j + 1;
					break;
				} else if (expression.substring(j, j + 1).equals("*")) {
					double y = stack.pop();
					double x = stack.pop();
					stack.push(multiply(x, y));
					i = j + 1;
					break;
				} else if (expression.substring(j, j + 1).equals("/")) {
					double y = stack.pop();
					double x = stack.pop();
					stack.push(divide(x, y));
					i = j + 1;
					break;
				} else if (expression.substring(j, j + 1).equals("%")) {
					System.out.println("Hello");
					double y = stack.pop();
					double x = stack.pop();
					stack.push(modulous(x, y));
					i = j + 1;
					break;
				} else if (expression.substring(j, j + 1).equals("^")) {
					double y = stack.pop();
					double x = stack.pop();
					stack.push(exponentiate(x, y));
					i = j + 1;
					break;
				}
				// TODO Calc: log calculation
				// else if (expression.substring(j) == "log") {
				//
				// }
				else if (expression.substring(j, j + 1).equals(" ")) {
					if (j != i) {
						stack.push(Double.valueOf(expression.substring(i, j)));
					}
					i = j + 1;
					break;
				}
				
			}
		}
		//Check if the expression has no operator
		if(stack.empty()){
			Double.valueOf(expression);
		}
		return stack.pop();
	}

	// Mathematics Operations
	private double add(double x, double y) {
		double num = x + y;
		return num;
	}

	public double subtract(double x, double y) {
		double num = x - y;
		return num;
	}

	private double multiply(double x, double y) {
		double num = x * y;
		return num;
	}

	private double divide(double x, double y) {
		if (y == 0) {
			logger.error("Calculator divisor 0 error");
			return 0;
		}
		double num = x / y;
		return num;
	}

	private double modulous(double x, double y) {
		if (y == 0) {
			logger.error("Calculator divisor 0 error");
			return 0;
		}
		System.out.println("Coool: " + x + " , " + y);
		double num = x % y;
		return num;
	}

	private double exponentiate(double x, double y) {
		return Math.pow(x, y);
	}

	// private double log(double x) {
	// return Math.log(x);
	// }u.3
	//
	// private double log10(double x) {
	// return Math.log10(x);
	// }

}
