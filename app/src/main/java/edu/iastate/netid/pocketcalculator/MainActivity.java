package edu.iastate.netid.pocketcalculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    /**
     * The instance of the calculator model for use by this controller.
     */
    private final CalculationStream mCalculationStream = new CalculationStream();

    /*
     * The instance of the calculator display TextView. You can use this to update the calculator display.
     */
    private TextView mCalculatorDisplay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCalculatorDisplay = findViewById(R.id.CalculatorDisplay);
    }

    public void onDigitClicked(View view) {
        Button button = (Button) view;
        String digitText = button.getText().toString();
        CalculationStream.Digit digit = CalculationStream.Digit.ZERO;
        switch (digitText) {
            case "1":
                digit = CalculationStream.Digit.ONE;
                break;
            case "2":
                digit = CalculationStream.Digit.TWO;
                break;
            case "3":
                digit = CalculationStream.Digit.THREE;
                break;
            case "4":
                digit = CalculationStream.Digit.FOUR;
                break;
            case "5":
                digit = CalculationStream.Digit.FIVE;
                break;
            case "6":
                digit = CalculationStream.Digit.SIX;
                break;
            case "7":
                digit = CalculationStream.Digit.SEVEN;
                break;
            case "8":
                digit = CalculationStream.Digit.EIGHT;
                break;
            case "9":
                digit = CalculationStream.Digit.NINE;
                break;
            case ".":
                digit = CalculationStream.Digit.DECIMAL;
                break;
        }
        mCalculationStream.inputDigit(digit);
        updateCalculatorDisplay();
    }

    public void onOperationClicked(View view) {
        Button button = (Button) view;
        String operationText = button.getText().toString();
        CalculationStream.Operation operation = CalculationStream.Operation.NONE;
        switch (operationText) {
            case "+":
                operation = CalculationStream.Operation.ADD;
                break;
            case "-":
                operation = CalculationStream.Operation.SUBTRACT;
                break;
            case "*":
                operation = CalculationStream.Operation.MULTIPLY;
                break;
            case "/":
                operation = CalculationStream.Operation.DIVIDE;
                break;
        }
        mCalculationStream.inputOperation(operation);
        updateCalculatorDisplay();
    }

    public void onClearClicked(View view) {
        mCalculationStream.clear();
        updateCalculatorDisplay();
    }

    public void onEqualClicked(View view) {
        try {
            mCalculationStream.calculateResult();
        } finally {
            updateCalculatorDisplay();
        }
    }

    /**
     * Call this method after every button press to update the text display of your calculator.
     */
    public void updateCalculatorDisplay() {
        String value = getString(R.string.empty);
        try {
            value = Double.toString(mCalculationStream.getCurrentOperand());
        } catch(NumberFormatException e) {
            value = getString(R.string.error);
        } finally {
            mCalculatorDisplay.setText(value);
        }
    }
}