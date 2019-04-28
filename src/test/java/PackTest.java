import com.mobiquityinc.packer.Packer;
import org.junit.Assert;
import org.junit.Test;

public class PackTest {

    @Test
    public void testPack(){
        String result = "3,2\n" +
                "-\n" +
                "-\n" +
                "4,3,2\n" +
                "-\n" +
                "2\n" +
                "9\n";

        Assert.assertEquals(Packer.pack("./src/test/resources/input.txt"),result);
    }
}
