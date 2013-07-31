package com.griddynamics.deming.ecommerce.api.endpoint.catalog;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import com.google.common.collect.Maps;
import com.griddynamics.deming.ecommerce.api.wrapper.ImportCategoryWrapper;
import com.griddynamics.deming.ecommerce.api.wrapper.ImportProductWrapper;
import com.griddynamics.deming.ecommerce.api.wrapper.SimpleCatalogWrapper;
import com.griddynamics.deming.ecommerce.cms.file.service.DmgStaticAssetStorageService;
import com.griddynamics.deming.ecommerce.cms.file.service.MultipartFileAdapter;
import com.griddynamics.deming.ecommerce.media.dao.MediaDao;
import com.sun.jersey.multipart.FormDataParam;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang3.StringEscapeUtils;
import org.broadleafcommerce.common.exception.ServiceException;
import org.broadleafcommerce.common.media.domain.Media;
import org.broadleafcommerce.common.media.domain.MediaImpl;
import org.broadleafcommerce.common.money.Money;
import org.broadleafcommerce.core.catalog.dao.ProductDao;
import org.broadleafcommerce.core.catalog.domain.*;
import org.broadleafcommerce.core.catalog.service.CatalogService;
import org.broadleafcommerce.core.catalog.service.type.ProductType;
import org.broadleafcommerce.core.search.service.SearchService;
import org.broadleafcommerce.core.web.api.endpoint.BaseEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * This is a reference REST API endpoint for catalog loader.
 * The purpose is to provide an out of the box RESTful catalog service implementation, but also
 * to allow the implementor to have fine control over the actual API, URIs, and general JAX-RS annotations.
 */
@Component
@Scope("singleton")
@Path("/catalog_manager/")
public class CatalogManagerEndpoint extends BaseEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(CatalogManagerEndpoint.class);

    private static final String PRODUCT_CATALOG_FILE = "product_catalog.json";

    @Resource(name="blSearchService")
    protected SearchService searchService;

    @Resource(name="blCatalogService")
    protected CatalogService catalogService;

    @Resource(name="blProductDao")
    protected ProductDao productDao;

    @Resource(name = "dmgStaticAssetStorageService")
    protected DmgStaticAssetStorageService dmgStaticAssetStorageService;

    @Resource(name="dmgMediaDao")
    protected MediaDao mediaDao;

    @GET
    @Path("rebuild_index") // todo: need remove from production (this need only for test)
    @Produces(value = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Consumes(value = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response rebuildSearchIndex() throws IOException, ServiceException {
        searchService.rebuildIndex();

        return Response.status(Response.Status.OK)
                .entity("Search index was rebuilt successfully!\n")
                .build();
    }

    @GET
    @Path("export")
    @Produces(value = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Consumes(value = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public SimpleCatalogWrapper exportCatalog(@Context HttpServletRequest request) {
        List<Category> categories = catalogService.findCategoriesByName("Root");

        Validate.notEmpty(categories, "Root category not found.");
        Validate.isTrue(categories.size() == 1, "Root category isn't unique category.");

        Category catalog = categories.get(0);

        SimpleCatalogWrapper wrapper = (SimpleCatalogWrapper) context.getBean(SimpleCatalogWrapper.class.getName());
        wrapper.wrapDetails(catalog, request);
        return wrapper;
    }

    @POST
    @Path("import")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response importCatalog(@FormDataParam("file") InputStream uploadInputStream) throws ServiceException, IOException {
        removeCatalog();

        try {
            ZipInputStream inputStream = new ZipInputStream(uploadInputStream);

            try {
                byte[] buf = new byte[1024];

                for (ZipEntry entry = inputStream.getNextEntry(); entry != null; entry = inputStream.getNextEntry()) {
                    try {
                        String entryName = entry.getName();
                        entryName = entryName.replace('/', File.separatorChar);
                        entryName = entryName.replace('\\', File.separatorChar);

                        LOGGER.debug("Entry name: {}", entryName);

                        if (entry.isDirectory()) {
                            LOGGER.debug("Entry ({}) is directory", entryName);
                        } else if (PRODUCT_CATALOG_FILE.equals(entryName)) {
                            ByteArrayOutputStream jsonBytes = new ByteArrayOutputStream(1024);

                            for (int n = inputStream.read(buf, 0, 1024); n > -1; n = inputStream.read(buf, 0, 1024)) {
                                jsonBytes.write(buf, 0, n);
                            }

                            byte[] bytes = jsonBytes.toByteArray();

                            ObjectMapper mapper = new ObjectMapper();

                            JaxbAnnotationModule jaxbAnnotationModule = new JaxbAnnotationModule();
                            mapper.registerModule(jaxbAnnotationModule);

                            ImportCategoryWrapper catalog = mapper.readValue(bytes, ImportCategoryWrapper.class);
                            escape(catalog);
                            saveCategoryTree(catalog);
                        } else {
                            MultipartFile file = new MultipartFileAdapter(inputStream, entryName);
                            dmgStaticAssetStorageService.createStaticAssetStorageFromFile(file);
                        }

                    } finally {
                        inputStream.closeEntry();
                    }
                }
            } finally {
                inputStream.close();
            }

        } catch (IOException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Unable load catalog.\n")
                    .build();
        }

        Thread rebuildSearchIndex = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                    searchService.rebuildIndex();
                } catch (Exception e) {/* nothing */}
            }
        });
        rebuildSearchIndex.start();

        return Response.status(Response.Status.OK)
                .entity("Catalog was imported successfully!\n")
                .build();
    }

    private void escape(ImportCategoryWrapper categoryWrapper) {
        categoryWrapper.name = StringEscapeUtils.escapeHtml4(categoryWrapper.name);
        categoryWrapper.description = StringEscapeUtils.escapeHtml4(categoryWrapper.description);

        if (CollectionUtils.isNotEmpty(categoryWrapper.products)) {
            for (ImportProductWrapper product : categoryWrapper.products) {
                product.name = StringEscapeUtils.escapeHtml4(product.name);
                product.description = StringEscapeUtils.escapeHtml4(product.description);
            }
        }

        if (CollectionUtils.isNotEmpty(categoryWrapper.subcategories)) {
            for (ImportCategoryWrapper subcategoryWrapper : categoryWrapper.subcategories) {
                escape(subcategoryWrapper);
            }
        }
    }

    private Category saveCategoryTree(ImportCategoryWrapper categoryWrapper) {
        Category category = catalogService.createCategory();
        category.setName(categoryWrapper.name);
        category.setDescription(categoryWrapper.description);
        category.setActiveStartDate(new Date());

        category = catalogService.saveCategory(category);

        if (CollectionUtils.isNotEmpty(categoryWrapper.products)) {
            for (ImportProductWrapper product : categoryWrapper.products) {
                Sku newSku = catalogService.createSku();
                newSku.setName(product.name);
                newSku.setLongDescription(product.description);
                newSku.setRetailPrice(new Money(23.4));
                newSku.setSalePrice(new Money(24.3));
                newSku.setActiveStartDate(new Date());
                newSku.setSkuMedia(createMediaMap(product));

                newSku = catalogService.saveSku(newSku);

                Product newProduct = catalogService.createProduct(ProductType.PRODUCT);
                newProduct.setDefaultSku(newSku);
                newProduct.setDefaultCategory(category);

                newProduct = catalogService.saveProduct(newProduct);

                newSku.setDefaultProduct(newProduct);
                catalogService.saveSku(newSku);

                CategoryProductXref categoryProductXref = new CategoryProductXrefImpl();
                categoryProductXref.setCategory(category);
                categoryProductXref.setProduct(newProduct);

                newProduct.getAllParentCategoryXrefs().add(categoryProductXref);
                newProduct.setUrl("/" + category.getId() + "/" + newProduct.getId());
                catalogService.saveProduct(newProduct);
            }
        }

        if (CollectionUtils.isNotEmpty(categoryWrapper.subcategories)) {
            for (ImportCategoryWrapper subcategoryWrapper : categoryWrapper.subcategories) {
                Category subcategory = saveCategoryTree(subcategoryWrapper);

                CategoryXref categoryXref = new CategoryXrefImpl();
                categoryXref.setCategory(category);
                categoryXref.setSubCategory(subcategory);

                category.getAllParentCategoryXrefs().add(categoryXref);
                subcategory.setDefaultParentCategory(category);
                catalogService.saveCategory(subcategory);
            }
        }

        category.setUrl("/" + category.getId());
        category = catalogService.saveCategory(category);

        return category;
    }

    private void removeCatalog() throws IOException, ServiceException {
        List<Category> rootCategories = catalogService.findCategoriesByName("Root");

        if (CollectionUtils.isNotEmpty(rootCategories)) {
            for (Category rootCategory : rootCategories) {
                rootCategory.setName("OldRoot");
                catalogService.saveCategory(rootCategory);
            }
        }

        for (Product product : catalogService.findAllProducts()) {
            productDao.delete(product);
        }

        for (Category category : catalogService.findAllCategories()) {
            catalogService.removeCategory(category);
        }
    }

    private Map<String, Media> createMediaMap(ImportProductWrapper productWrapper) {
        Map<String,Media> mediaMap = Maps.newHashMap();

        String paths = productWrapper.image.replace('/', File.separatorChar).replace('\\', File.separatorChar);
        String[] imagesPaths = paths.split("\\s*;\\s*");

        for (int i = 0; i < imagesPaths.length; ++i) {
            String imagePath = "/cmsstatic/" + imagesPaths[i];
            String imagePriority = i > 0 ? "alt" + i : "primary";

            Media media = new MediaImpl();
            media.setUrl(imagePath);
            media.setTitle(productWrapper.name);
            media.setAltText(imagePriority);

            media = mediaDao.save(media);

            mediaMap.put(imagePriority, media);
        }

        return mediaMap;
    }
}
