File formats
============

Product catalog for web store
-----------------------------

Product catalog for web store is zip archive file that contains *product_catalog.json* file and directory with images.

Structure of zip archive:

    product-catalog.zip
     |
     -- product_catalog.json
     -- root/
        |
        -- category_1
        |  |
        |  -- product_1_1.jpg
        |  -- product_1_2.jpg
        |  -- ...
        -- category_2
        |  |
        |  -- product_2_1.jpg
        |  -- ...
        ...


*product_catalog.json* consist of categories tree.
Each category contains following fields:

 - **_name_** - category name (not empty value)
 - **_description_** - category description (nullable value)
 - **_subcategories_** - list of subcategories of this category (nullable value)
 - **_products_** - list of products of this category (nullable value)

Each product contains following fields:

 - **_name_** - product name (not empty value)
 - **_description_** - product description (nullable value)
 - **_image_** - list of path to images from directory separated by “;”


Example of *product_catalog.json*:

    {
        "name": "Root",
        "description": "Root category",
        "subcategories": [
            {
                "name": "Category 1",
                "description": "Description of category 1",
                "subcategories": [
                    ...
                ],
                "products": [
                    {
                        "name": "product 1",
                        "description": "Description of product 1",
                        "image": "root/category_1/product1_img1.jpg; root/category_1/product1_img2.jpg"
                    },
                    {
                        "name": "product 2",
                        "description": "Description of product 2",
                        "image": "root/category_1/product1.jpg"
                    },
                    ...
                ]
            },
            ...
        ]
    }

Directory with images contains images in jpeg or png format.

We have created [product catalog](https://s3.amazonaws.com/gd-bask/magento_catalog.zip) based on
[magento catalog](http://www.magentocommerce.com/knowledge-base/entry/installing-the-sample-data-for-magento).


Product catalog information for transaction log generator
---------------------------------------------------------

Product catalog information contains product catalog in json format that consist of categories tree.
Each category contains following fields:

 - **_id_** - category id in web store (not null value)
 - **_name_** - category name in web store (not null value)
 - **_subcategories_** - list of subcategories of this category (nullable value)
 - **_products_** - list of products of this category (nullable value)


Each product contains following fields:

 - **_id_** - product id in web store (not null value)
 - **_name_** - product name in web store (not null value)

Example of *product-catalog-info.json*:

    {
        "id": 1,
        "name": "Root",
        "subcategories": [
            {
                "id": 2,
                "name": "Category 1",
                "subcategories": [
                    ...
                ],
                "products": [
                    {
                        "id": 21,
                        "name": "product 1"
                    },
                    {
                        "id": 22,
                        "name": "product 2"
                    },
                    ...
                ]
            },
            ...
        ]
    }

You can see more real example from source code:
[product-catalog-information.json](../maven_projects/dataset-generator/src/main/resources/product-catalog.json)


Transaction log
---------------

Transaction log is text file that contains transactions separated by newline char.
Each transaction is list of product ids that separated by a comma, a space char or combination of comma and space.

Example:

    123,334,23423,45,453,44
    3432,323,33
    234,885
    ...


Recommendations file
--------------------

Recommendation file is text file that contains recommendation in the form of a dictionary,
where key and value separated by tab char. Dictionary records is divided by newline char.
Key and value contain product ids divided by comma.

Example:

    4,12    23,57,17
    55,73,98    24,84
    17    4,12,23,57,93
    ...
