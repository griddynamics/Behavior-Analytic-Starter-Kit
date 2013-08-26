package com.griddynamics.deming.ecommerce.cms.file.service;

import org.broadleafcommerce.cms.file.domain.StaticAsset;
import org.broadleafcommerce.cms.file.service.StaticAssetService;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

public class DmgStaticAssetStorageServiceImpl implements DmgStaticAssetStorageService {

    @Resource(name="blStaticAssetStorageService")
    protected org.broadleafcommerce.cms.file.service.StaticAssetStorageService staticAssetStorageService;

    @Resource(name = "blStaticAssetService")
    protected StaticAssetService staticAssetService;

    public StaticAsset createStaticAssetStorageFromFile(MultipartFile file) throws IOException {
        StaticAsset staticAsset = staticAssetService.createStaticAssetFromFile(file, null);
        staticAssetStorageService.createStaticAssetStorageFromFile(file, staticAsset);

        return staticAsset;
    }
}
