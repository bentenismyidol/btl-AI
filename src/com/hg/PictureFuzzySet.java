package com.hg;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.rule.Variable;

import java.util.Random;

public class PictureFuzzySet {
    private double result;

    private double Obstacles_value;

    public double getObstacles_value() {
        return Obstacles_value;
    }

    public void setObstacles_value(double obstacles_value) {
        Obstacles_value = obstacles_value;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }

        public double FuzzyLogic() {
            // Load the FCL file
            FIS fis = FIS.load("D:\\BTL AI 2\\BTL_ATTGDDT_N2 (1)\\BTL_ATTGDDT_N2\\robot\\test\\Rules.fcl", true);

            // Get the function block
            FunctionBlock functionBlock = fis.getFunctionBlock("Rules");

            // Set the input variable

                Random rd = new Random();
                double n = rd.nextDouble();
                double a = rd.nextDouble() * 100;
                functionBlock.setVariable("Obstacles", n);
                functionBlock.setVariable("Distance",a);
                // Evaluate the FCL
                functionBlock.evaluate();

                // Get the output variable
                Variable Direction = functionBlock.getVariable("Direction");
                result = Direction.getValue();
                // Print the output value (for testing purpose)
                return result;

        }

    public static void main(String[] args) {
        PictureFuzzySet PFS = new PictureFuzzySet();
        while (true)
        System.out.println(PFS.FuzzyLogic());
    }
}



