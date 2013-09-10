Feature: Web Store

  Scenario: Start Web Store instance
    Given I have a qubell platform
    When I start new instance of 'Web Store'
    Then I wait when instance status will changed from 'Requested' to 'Running'

  Scenario: Save product catalog structure to external storage
    When I run workflow 'Save_product_catalog_structure_to_S3'
    Then I wait when instance status will changed from 'Executing' to 'Running'
