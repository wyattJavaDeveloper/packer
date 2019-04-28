import com.mobiquityinc.domain.BestValuePack;
import com.mobiquityinc.domain.Item;
import com.mobiquityinc.domain.Pack;
import com.mobiquityinc.processor.PackageProcessor;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class PackageProcessorTest {


    @Test
    public void favorLowestWeightForTheSameCost(){
        List<Item> items = Arrays.asList(
                new Item(1,new BigDecimal("5"),new BigDecimal("200")),
                new Item(2,new BigDecimal("8.34"),new BigDecimal("10")),
                new Item(3,new BigDecimal("8.35"),new BigDecimal("10"))
        );

        Pack pack = new Pack(20,items);
        Assert.assertEquals(PackageProcessor.getInstance().calculateBestValuePack(pack).toString(),"2,1");
    }

    @Test
    public void testPackBestValuePack(){
        List<Item> items = Arrays.asList(
                new Item(1,new BigDecimal("5"),new BigDecimal("200")),
                new Item(2,new BigDecimal("10"),new BigDecimal("10")),
                new Item(3,new BigDecimal("8"),new BigDecimal("15"))
        );

        Pack pack = new Pack(20,items);
        BestValuePack bestValuePack = PackageProcessor.getInstance().calculateBestValuePack(pack);
        Assert.assertEquals(bestValuePack.getItems().size(),2);
        Assert.assertEquals(bestValuePack.getItems().get(0).getIndexNumber(), 3);
        Assert.assertEquals(bestValuePack.getItems().get(1).getIndexNumber(), 1);

    }
}
