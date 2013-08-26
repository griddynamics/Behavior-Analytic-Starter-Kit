package com.griddynamics.deming.ecommerce.cms.file.service;

import org.broadleafcommerce.cms.file.domain.StaticAsset;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface DmgStaticAssetStorageService {

    StaticAsset createStaticAssetStorageFromFile(MultipartFile file) throws IOException;
}
