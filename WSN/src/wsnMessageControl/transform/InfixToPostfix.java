package wsnMessageControl.transform;

import java.util.Stack;

public class InfixToPostfix {
	   private static boolean isOperator(char c) { return "+-*/%^".indexOf(c) != -1; }
	   
	   private static boolean isOperand(char c) { return (c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z'); }
	 
	   private static boolean hasLessPrecedence(char c, char d)
	   {
	      return "+*-/".indexOf(c)%2 < "+*-/".indexOf(d)%2;
	   }
	 
	   public static String convert(String exp)
	   {
	      Stack<Character> stack = new Stack<Character>();
	      String result = new String();
	      boolean continuous = false;
	 
	      for(char c : exp.toCharArray())
	      {
	         if (c  == ' ') continuous = false;
	 
	         else if (c == '(') stack.push(c);
	 
	         else if (isOperand(c))
	         {
	            result += (continuous?"":" ")+c;
	            continuous = true;
	         }
	 
	         else if (c == ')')
	         {
	            while(stack.peek() != '(') result += " "+stack.pop();
	            stack.pop();
	         }
	 
	         else if (isOperator(c))
	         {
	            while(!stack.isEmpty() && c != '(' && !hasLessPrecedence(stack.peek(), c))
	               result += " "+stack.pop();
	            stack.push(c);
	            continuous = false;
	         }
	 
	         else throw new Error("Invalid operand or operator: "+c);
	      }
	 
	      while (!stack.isEmpty()) result += " "+stack.pop();
	 
	      return result.trim();
	   }

}
