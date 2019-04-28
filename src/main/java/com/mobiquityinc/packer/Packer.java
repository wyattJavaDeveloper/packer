package com.mobiquityinc.packer;

import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.domain.BestValuePack;
import com.mobiquityinc.domain.Pack;
import com.mobiquityinc.processor.FileProcessor;
import com.mobiquityinc.processor.PackageProcessor;

import java.util.List;

/**
 * Class responsible for accessing the packer capabilities.
 * APIException will be thrown if the file cannot be parsed or the validation constraints of the content is not met.
 */
public class Packer {

    /**
     * @param fileLocation
     * @return
     * @throws APIException
     */
    public static String pack(String fileLocation) throws APIException {

        List<Pack> packsToProcess = FileProcessor.getInstance().processFile(fileLocation);
        StringBuilder result = new StringBuilder();

        for (Pack pack : packsToProcess) {
            BestValuePack bestValuePack = PackageProcessor.getInstance().calculateBestValuePack(pack);
            result.append(bestValuePack.toString() + "\n");
        }

        return result.toString();
    }
}
