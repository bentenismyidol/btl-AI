package com.hg;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.rule.Variable;

import java.util.Random;

public class TestPFSRules {
    private double result;

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }

    public int FuzzyLogic() {
        // Load the FCL file
        FIS fis = FIS.load("D:\\BTL AI 2\\BTL_ATTGDDT_N2 (1)\\BTL_ATTGDDT_N2\\robot\\test\\TestRules.fcl", true);

        // Get the function block
        FunctionBlock functionBlock = fis.getFunctionBlock("dijkstra");

        // Set the input variable

        Random rd = new Random();
        double n = rd.nextDouble();
        double a = rd.nextDouble() * 100;
        functionBlock.setVariable("obstacle", n);
        // Evaluate the FCL
        functionBlock.evaluate();

        // Get the output variable
        Variable Direction = functionBlock.getVariable("distance");
        result = Direction.getValue();
        // Print the output value (for testing purpose)
        return (int) result;

    }

    public static void main(String[] args) {
        PictureFuzzySet PFS = new PictureFuzzySet();
        while (true)
            System.out.println(PFS.FuzzyLogic());
    }
}