/*
* Copyright 2013 Grid Dynamics, Inc.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

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
