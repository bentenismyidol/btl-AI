package com.hg;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.rule.Variable;

public class PictureFuzzySet {
    public void FuzzyLogic() {
        // Load the FCL file
        FIS fis = FIS.load("D:\\BTL AI 2\\BTL_ATTGDDT_N2 (1)\\BTL_ATTGDDT_N2\\robot\\test\\Rules.fcl", true);

        // Get the function block
        FunctionBlock functionBlock = fis.getFunctionBlock("Rules");

        // Set the input variable
        functionBlock.setVariable("Obstacles", 3.2);

        // Evaluate the FCL
        functionBlock.evaluate();

        // Get the output variable
        Variable Direction = functionBlock.getVariable("Direction");
        double result = Direction.getValue();
        // Print the output value (for testing purpose)
        System.out.println("Direction Index: " + result);
    }
}



