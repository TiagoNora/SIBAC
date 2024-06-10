package fcl;
import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.rule.Variable;

public class FuzzyLogicHelper {
    private FIS fis;

    public FuzzyLogicHelper(String fclFilePath) {
        fis = FIS.load(fclFilePath, true);
        if (fis == null) {
            throw new RuntimeException("Error loading FCL file: " + fclFilePath);
        }
    }

    public double getViability(double age, double physicalCondition) {
        fis.setVariable("age", age);
        fis.setVariable("physicalCondition", physicalCondition);
        fis.evaluate();
        Variable viability = fis.getVariable("risk");
        return viability.getValue();
    }
}
