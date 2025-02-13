import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import weka.core.converters.ConverterUtils.DataSource;
import java.util.Random;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;

public class kfCV {

    public static void main(String[] args) throws Exception {
        Instances data = loadData("/home/ibai/Deskargak/heart-c.arff");
        if (data == null) return;
        String outputPath = "/home/ibai/Dokumentuak/weka/k-fold-results.txt";
        int k = 5;
        kfCV.erabili_kFold(data, outputPath, args, k);
    }

    public static void erabili_kFold(Instances data, String outputPath, String[] args, int k) throws Exception {
        NaiveBayes estimator = new NaiveBayes();
        Evaluation evaluator = new Evaluation(data);
        evaluator.crossValidateModel(estimator, data, k, new Random(1));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            writer.write("Execution Date: " + new Date() + "\n");
            writer.write("Execution Arguments: " + String.join(", ", args) + "\n\n");
            writer.write("Confusion Matrix:\n");
            double[][] confMatrix = evaluator.confusionMatrix();
            for (double[] row : confMatrix) {
                for (double value : row) {
                    writer.write(value + "\t");
                }
                writer.write("\n");
            }
            writer.write("\n");

            writer.write("Precision Metrics:\n");
            for (int i = 0; i < data.numClasses(); i++) {
                writer.write("Class " + i + ": " + evaluator.precision(i) + "\n");
            }
            writer.write("Weighted Avg: " + evaluator.weightedPrecision() + "\n\n");

            writer.write("Evaluation Results:\n");
            writer.write("Correctly Classified Instances: " + evaluator.pctCorrect() + "\n");
            writer.write("Incorrectly Classified Instances: " + evaluator.pctIncorrect() + "\n");
            writer.write("Kappa Statistic: " + evaluator.kappa() + "\n");
            writer.write("Mean Absolute Error: " + evaluator.meanAbsoluteError() + "\n");
            writer.write("Root Mean Squared Error: " + evaluator.rootMeanSquaredError() + "\n");
            writer.write("Relative Absolute Error: " + evaluator.relativeAbsoluteError() + "\n");
            writer.write("Root Relative Squared Error: " + evaluator.rootRelativeSquaredError() + "\n");
        } catch (IOException e) {
            System.out.println("ERROR: Unable to write to the file: " + outputPath);
        }
    }

    private static Instances loadData(String filePath) {
        DataSource source = null;
        try {
            source = new DataSource(filePath);
        } catch (Exception e) {
            System.out.println("ERROR: File not found: " + filePath);
            return null;
        }
        Instances data = null;
        try {
            data = source.getDataSet();
        } catch (Exception e) {
            System.out.println("ERROR: Unable to read the file: " + filePath);
            return null;
        }
        
        if (data.classIndex() == -1)
            data.setClassIndex(data.numAttributes() - 1);
        
        return data;
    }
}
