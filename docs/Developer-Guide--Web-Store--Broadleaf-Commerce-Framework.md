Broadleaf Commerce framework
============================

+ [Summary](#summary)
+ [Technology stack](#technology-stack)
+ [Features](#features)
+ [Architecture](#architecture)
+ [Extending methods](#extending-methods)
+ [Heat Clinic web app](#heat-clinic-web-app)
+ [Workflow and Activities](#workflow-and-Activities)
+ [Catalog and Search](#catalog-and-search)
+ [Presentation Layer](#presentation-layer)
+ [REST Web Service API](#rest-web-service-api)
+ [Internationalization](#internationalization)
+ [Security](#security)
+ [Also See](#also-see)

Summary
-------
BroadleafCommerce is an open-source, enterprise-class e-commerce framework
that has been designed from the ground up for scalability and customizability.
It has demo web application - [Heat Clinic](http://demo.broadleafcommerce.org/heatclinic/).

**_Web site:_** [www.broadleafcommerce.org](http://www.broadleafcommerce.org/) <br/>
**_Current version:_** 3.0 (June, 2013)<br/>
**_Written in:_** Java <br/>
**_Operating system:_** Cross-platform <br/>
**_Licence:_** Apache 2.0

Technology stack
----------------
* Java
* Spring framework (DI, JPA support, Transactions, aop, JMS, mvc, web-flow, social, security and other)
* Hibernate (JPA provider + cache)
* Thymeleaf (nature templating)
* JAX-RS (rest api)
* Apache Solr (faceted search)
* Quartz (task scheduling)
* Tomcat (servlet container)
* Maven (build system)

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
* Checkout
    - Payment
    - Basic Tax Module
* Administration console
* Content Management


Architecture
------------
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
* Core module
* Site module
* Admin module

Workflow and Activities
-----------------------
TODO:

Catalog and Search
------------------
TODO:

Presentation Layer
------------------
TODO:

REST Web Service API
--------------------
TODO:

Internationalization
--------------------
TODO:

Security
--------
TODO:

Also See
--------
* [Broadleaf documentation](http://docs.broadleafcommerce.org/core/current)
