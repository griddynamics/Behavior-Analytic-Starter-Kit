
REST API of Web Store
---------------------

### Import product catalog

You can import catalog by curl:

`curl -F "file=@product-catalog.zip" http://localhost:8080/api/v1/catalog_manager/import`

Format of *product-catalog.zip* you can see to
[Product catalog for web store](Developer-Guide.md#product-catalog-for-web-store)


### Export product catalog information

You can export product catalog information by curl:

`curl -X GET http://localhost:8080/api/v1/catalog_manager/export > product-catalog-info.json`

Format of *product-catalog-info.json* you can see to
[Product catalog information for transaction log generator](Developer-Guide.md#product-catalog-information-for-transaction-log-generator)


### Import recommendations

You can import recommendations by curl:

`curl -F "file=@recommendations.txt" http://localhost:8080/api/v1/recommendations/import`

Format of *recommendations.txt* you can see to
[Recommendations file](Developer-Guide.md#recommendations-file)
