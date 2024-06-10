package org.sibac.fcl;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.rule.Variable;
import org.sibac.model.Conclusion;

import java.io.File;
import java.io.InputStream;

public class FuzzyLogicHelper {
    private FIS fis;

    public FuzzyLogicHelper(String fclFileName) {
        // Use class loader to get the resource


        // Load the FCL file
        FIS fis = FIS.load(fclFileName,true);
        if (fis == null) {
            throw new RuntimeException("Error loading FCL file: " + fclFileName);
        }

        // Print the directory containing the FuzzyLogicHelper class
        try {
            String classPath = new File(FuzzyLogicHelper.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
            System.out.println("Directory containing the FuzzyLogicHelper class: " + classPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

        public double getViability(double age, double physicalCondition) {
        fis.setVariable("age", age);
        fis.setVariable("physicalCondition", physicalCondition);
        fis.evaluate();
        Variable viability = fis.getVariable("risk");
        return viability.getValue();
    }

    public Conclusion returnconclusion(double viability){
        Conclusion c;
        if (viability > 50) {
            c= new Conclusion(Conclusion.APTO);
        } else {
            c = new Conclusion(Conclusion.INAPTO);
        }

        return c;
    }
}
