package edu.iastate.netid.pocketcalculator;

/**
 * A class that represents a left-to-right calculation inputted as a stream, ignoring order
 * of operations.
 */
public class CalculationStream {

    /**
     * The operations supported by the calculation stream.
     */
    public enum Operation {
        NONE,
        ADD,
        SUBTRACT,
        MULTIPLY,
        DIVIDE
    }

    /**
     * The "digits" supported by the operation stream (including decimal point).
     */
    public enum Digit {
        ZERO,
        ONE,
        TWO,
        THREE,
        FOUR,
        FIVE,
        SIX,
        SEVEN,
        EIGHT,
        NINE,
        DECIMAL
    }

    /**
     * A string builder to represent the first number entered in the series of entries
     */
    private StringBuilder mExpression1;

    /**
     * A string builder to represent the second number entered in the series of entries
     */
    private StringBuilder mExpression2;

    /**
     * The current operation in the stream, i.e., what operation to perform on the two
     * expressions when the result needs to be calculated.
     */
    private Operation mCurrentOperation;

    /**
     * A flag to indicate if a calculation has just been performed.
     */
    private boolean mJustCalculated;


    CalculationStream() {
        mExpression1 = new StringBuilder();
        mExpression2 = new StringBuilder();
        mCurrentOperation = Operation.NONE;
        mJustCalculated = false;
    }

    /**
     * Appends a new digit to the active expression in the stream.
     * @param digit the digit to append
     */
    public void inputDigit(Digit digit) {
        if (mJustCalculated) {
            clear();
        }
        StringBuilder expr;
        if(mCurrentOperation != Operation.NONE) {
            expr = mExpression2;
        } else {
            expr = mExpression1;
        }
        switch(digit) {
            case ZERO:
                expr.append("0");
                break;
            case ONE:
                expr.append("1");
                break;
            case TWO:
                expr.append("2");
                break;
            case THREE:
                expr.append("3");
                break;
            case FOUR:
                expr.append("4");
                break;
            case FIVE:
                expr.append("5");
                break;
            case SIX:
                expr.append("6");
                break;
            case SEVEN:
                expr.append("7");
                break;
            case EIGHT:
                expr.append("8");
                break;
            case NINE:
                expr.append("9");
                break;
            case DECIMAL:
                if(expr.length() == 0) {
                    expr.append("0");
                }
                expr.append(".");
                break;
        }
    }

    /**
     * Sets the current operation. May first calculate the result of the previous current operation,
     * if needed.
     * @param operation the new current operation
     */
    public void inputOperation(Operation operation) {
        if(mExpression1.length() == 0) {
            mCurrentOperation = operation.NONE;
            return;
        }
        if(mCurrentOperation != Operation.NONE && mExpression2.length() > 0) {
            calculateResult();
        }
        mCurrentOperation = operation;
        mJustCalculated = false;
    }

    /**
     * Calculates the current result of the stream and prepares for a new expression.
     * @throws NumberFormatException
     */
    public void calculateResult() throws NumberFormatException {
        try {
            if(mExpression1.length() == 0 || mExpression2.length() == 0) {
                return;
            }
            double op1 = Double.parseDouble(mExpression1.toString());
            double op2 = Double.parseDouble(mExpression2.toString());
            double result = 0.0;
            switch(mCurrentOperation) {
                case ADD:
                    result = op1 + op2;
                    break;
                case SUBTRACT:
                    result = op1 - op2;
                    break;
                case MULTIPLY:
                    result = op1 * op2;
                    break;
                case DIVIDE:
                    result = op1 / op2;
                    break;
                case NONE:
                default:
                    result = op1;
            }

            clear();

            mExpression1 = new StringBuilder(Double.toString(result));
            mJustCalculated = true;

        } catch(NumberFormatException e) {
            throw e;
        }
    }

    /**
     * Clears the calculation stream.
     */
    public void clear() {
        mExpression1 = new StringBuilder();
        mExpression2 = new StringBuilder();
        mCurrentOperation = Operation.NONE;
        mJustCalculated = false;
    }

    /**
     * Gets the value of the current operand being entered. More specifically, this is the second
     * operand, if the second operand has a value. If not, this is the first operand. If neither
     * operands have a value, this returns zero. Useful for displaying the calculation stream in
     * a calculator-like format.
     * @return the value of the current operand
     * @throws NumberFormatException
     */
    public double getCurrentOperand() throws NumberFormatException {
        try {
            if (mCurrentOperation == Operation.NONE || mExpression2.toString().isEmpty()) {
                if(mExpression1.toString().isEmpty()) {
                    return 0.0;
                }
                return Double.parseDouble(mExpression1.toString());
            } else {
                return Double.parseDouble(mExpression2.toString());
            }
        } catch(NumberFormatException e) {
            throw e;
        }
    }

    //TODO - add other calculator logic methods you want to use in this calculator model class
}