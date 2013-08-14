Broadleaf Commerce framework
============================

BroadleafCommerce is an open-source, enterprise-class e-commerce framework
that has been designed from the ground up for scalability and customizability.
It has demo web application - [Heat Clinic](http://demo.broadleafcommerce.org/heatclinic/).

**_Web site:_** [www.broadleafcommerce.org](http://www.broadleafcommerce.org/) <br/>
**_Current version:_** 3.0 (June, 2013)<br/>
**_Written in:_** Java <br/>
**_Operating system:_** Cross-platform <br/>
**_Licence:_** Apache 2.0

Content
-------
+ [Technology stack](#technology-stack)
+ [Features](#features)
+ [Architecture](#architecture)
+ [Project Modules](#project-Modules)
+ [Extending methods](#extending-methods)
+ [Heat Clinic web app](#heat-clinic-web-app)
+ [Also See](#also-see)

Technology stack
----------------
* [Java](http://www.oracle.com/technetwork/java/index.html) - main programming language
* [Spring framework](http://www.springsource.org) - DI, JPA support, Transactions, AOP,
    JMS, MVC, social, security and other
* [Hibernate](http://www.hibernate.org) - JPA provider
* [Thymeleaf](http://www.thymeleaf.org) - template engine with natural templates
* [JAX-RS](http://jax-rs-spec.java.net) - rest api
* [Apache Solr](http://lucene.apache.org/solr) - faceted search
* [Quartz](http://quartz-scheduler.org) - job scheduler
* [Apache Tomcat](http://tomcat.apache.org) - servlet container
* [Apache Maven](http://maven.apache.org) - build system

Features
--------
* Catalog
    - categories
    - Products and Bundles
    - SKUs
    - Faceted Search
* Customer
    - Profile
    - Security
    - Order History
* Shopping Cart
* Dynamics Catalog Pricing
* Basic Promotion System
* Cross Sell / Up Sell
* Basic Fulfillment
* Basic Inventory
* Internationalization
    - Local Resolution
    - Currency Resolution
    - Translations
        + Category Data
        + Product Options
        + Product Option Values
        + Product Sku
    - CMS
* Checkout
    - Payment
    - Basic Tax Module
* Administration console
* Content Management

Architecture
------------

![Broadleaf Architecture](https://raw.github.com/griddynamics/Behavior-Analytic-Starter-Kit/master/docs/images/broadleaf_architecture.png)

Project Modules
---------------
* broadleaf-common (classes shared by various modules)
* broadleaf-framework (core classes)
* broadleaf-framework-web (Spring MVC controllers and related items)
* broadleaf-profile (Customer profile related classes, utility classes, email, configuration merge)
* broadleaf-profile-web (Spring MVC controllers and related items supporting the profile module)
* broadleaf-instrument (Allows for runtime instrumentation to override certain Broadleaf annotations)
* broadleaf-open-admin-platform (Framework for creating extensible administration GUIs for Hibernate managed domains)
* broadleaf-contentmanagement-module (full-featured content management system that is managed via the administration tool)
* broadleaf-admin-module (specific administration module that plugs into the open admin platform)

Extending methods
-----------------
* Configuration Merging
* Persistence Configuration
* Runtime Configuration Management
* Customizable Administration Platform
* Add-on Modules

Heat Clinic web app
-------------------
Heat Clinic contains three modules:
* Core module
* Site module
* Admin module

Core module common classes and resources which are used in other modules.
Admin module uses core module and implements admin console.
Site module uses core module and implements shopping cart application.

Also See
--------
* [Broadleaf documentation](http://docs.broadleafcommerce.org/core/current)
