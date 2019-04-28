import com.mobiquityinc.domain.Pack;
import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.processor.FileProcessor;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileProcessorTest {

    private static final String TEST_FILE_PATH = "./temp-test.txt";

    @After
    public void cleanUp() throws IOException {
        Path fileToDeletePath = Paths.get(TEST_FILE_PATH);
        Files.delete(fileToDeletePath);
    }

    @Test
    public void successfulRead() throws IOException {
        String input = "100 : (1,85.31,€29) (1,85.31,€30)\n100 : (1,85.31,€29) (1,85.31,€30)";
        Path testFile = Files.write(Paths.get(TEST_FILE_PATH), input.getBytes());

        List<Pack> packs = FileProcessor.getInstance().processFile(testFile.toString());

        Assert.assertEquals(packs.size(), 2);
        Assert.assertEquals(packs.get(0).getCapacity(), 100);
        Assert.assertEquals(packs.get(0).getItems().size(), 2);
        Assert.assertEquals(packs.get(0).getItems().get(0).getIndexNumber(), 1);
        Assert.assertEquals(packs.get(0).getItems().get(0).getWeight(), new BigDecimal("85.31"));
        Assert.assertEquals(packs.get(0).getItems().get(0).getCost(), new BigDecimal("29"));
    }

    @Test(expected = APIException.class)
    public void maxNumberOfItemsExceeded() throws IOException {
        String input = "100 : (1,15,€29) (1,15,€29) (1,15,€29) (1,15,€29) (1,15,€29) (1,15,€29) (1,15,€29) (1,15,€29) (1,15,€29) (1,15,€29) (1,15,€29) (1,15,€29) (1,15,€29)" +
                "(1,15,€29) (1,15,€29) (1,15,€29) (1,15,€29)";
        Path testFile = Files.write(Paths.get(TEST_FILE_PATH), input.getBytes());

        try {
            FileProcessor.getInstance().processFile(testFile.toString());
        } catch (APIException api) {
            Assert.assertTrue(api.getMessage().contains("Total number of items 17 exceeds the maximum of 15"));
            throw api;
        }
    }

    @Test(expected = APIException.class)
    public void packWeightExceedsMax() throws IOException {
        String input = "101 : (1,85.31,€29)";
        Path testFile = Files.write(Paths.get(TEST_FILE_PATH), input.getBytes());

        try {
            FileProcessor.getInstance().processFile(testFile.toString());
        } catch (APIException api) {
            Assert.assertTrue(api.getMessage().contains("Package weight 101 exceeds the maximum package weight of 100"));
            throw api;
        }
    }

    @Test(expected = APIException.class)
    public void itemWeightExceedsMax() throws IOException {
        String input = "100 : (1,101.31,€29)";
        Path testFile = Files.write(Paths.get(TEST_FILE_PATH), input.getBytes());

        try {
            FileProcessor.getInstance().processFile(testFile.toString());
        } catch (APIException api) {
            Assert.assertTrue(api.getMessage().contains("Item weight 101.31 exceeds the maximum item weight of 100"));
            throw api;
        }
    }

    @Test(expected = APIException.class)
    public void itemCostExceedsMax() throws IOException {
        String input = "100 : (1,99.31,€101)";
        Path testFile = Files.write(Paths.get(TEST_FILE_PATH), input.getBytes());

        try {
            FileProcessor.getInstance().processFile(testFile.toString());
        } catch (APIException api) {
            Assert.assertTrue(api.getMessage().contains("Item cost 101 exceeds the maximum item cost of 100"));
            throw api;
        }
    }

}
