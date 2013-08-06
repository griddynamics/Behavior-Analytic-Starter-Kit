Recommendation engine
=====================

Implementation
--------------

To implemnt recommendation engine in core module of Web Store we have added:

1. RecommendedProductImpl entity class that implement RecommendedProduct interface.
1. DmgOrder entity class. It extends OrderImpl class adding transient list of RecommendedProduct.
1. RecommendationServiceImpl service class that implement RecommendationServce interface.

To show recommendations with products in cart in site module of Web Store we have added:

1. DmgDialect class that extends AbstractDialect from thymeleaf framework.
1. RecommendationProcessor that allows in html-templates get recommendations.
1. addRecommendation rest method to CartController

Also we have changed cartOperations.js to add an ability adding of products from recommendations.

To load new recommendations to Web Store we have added
[function of import recommendations via REST API](Developer-Guide--Web-Store--REST-API.md#import-recommendations).
