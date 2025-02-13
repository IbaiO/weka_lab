import java.io.FileNotFoundException;
import java.io.IOException;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class DataMiningExample {
	
    public static void main(String[] args) throws Exception {
        Instances data = loadData("/home/ibai/Deskargak/heart-c.arff");
        if (data == null) return;
        kfCV.perform5FoldCV(data, args[1], args);
    }
    
    private static Instances loadData(String filePath) {
        DataSource source = null;
        try {
            source = new DataSource(filePath);
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: File not found: " + filePath);
            return null;
        }
        
        Instances data = null;
        try {
            data = source.getDataSet();
        } catch (IOException e) {
            System.out.println("ERROR: Unable to read the file: " + filePath);
            return null;
        }
        
        if (data.classIndex() == -1)
            data.setClassIndex(data.numAttributes() - 1);
        
        return data;
    }
}
