## Savagerow
Opensource SQLite-based Alternative for building UI-based linear workflow lowcode/nocode apps.

![https://circleci.com/gh/ssi-schaefer-nl/savagerow](https://circleci.com/gh/ssi-schaefer-nl/savagerow.svg?style=svg)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/7fcb1b3d73d74a6282e8d9d3e9c55890)](https://www.codacy.com/gh/ssi-schaefer-nl/savagerow/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=ssi-schaefer-nl/savagerow&amp;utm_campaign=Badge_Grade)

### Motivation
While building user interfaces and apps around logistic processes, ERP systems etc. 
Most of the users came from the world of spreadsheets and flatfiles. Existing ERP systems, legacy interfaces with lot of dials requires training and quite some overhead.
With that in mind we started the journey of SavageRow, inspired by airtable and other lowcode/nocode platforms.
Lot of installation in warehouses, industrial infrastructure are airgapped, therefore you can deploy the platform on-premise.

We see the usage of this platform beyond ERP systems, logistic process etc. As the product reached minimum viable product status, therefore we decided to publish it.

### Features
#### Add a database
SavageRow is based on SQLite databases. You can easily add your own SQLite database to the workspace and start using it in SavageRow without any configuration. 

If you don't have an existing database, or if you want to create a new database, you can create a new database in the workspace configuration page. After that, you are ready to switch to the tables page, and start adding tables and creating columns in those tables. 


#### Simple Data Manipulation
SavageRow makes it easy to inspect and manipulate the data that you store. It features an experience similar to spreadsheets and allows you to easily add or delete columns and tables. Any change you make in your rows are immediately stored and updated. When you insert a new row, you can specify all the desired field values and easily save the row by the right-click menu available in the table.

When dealing with columns that have foreign key relationships, SavageRow allows you to easily alter the referenced row by providing a pop-up window showing you all the rows that can be referenced.

#### Workflows
Currently SavageRow provides only one type of workflows, called Simple Workflows. Simple Workflows allow you to define a sequence of actions that must be taken when an entry in a table is either added, changed or deleted. Actions include CRUD-like operations such as deleting, inserting and updating other rows in the same database, as well as the option to perform API Calls with JSON content. Moreover, Simple Workflows can be conditionally triggered by defining criteria that must be satisfied in order for the workflow to be executed.

The actions of Simple Workflows are basic: they perform a simple task and will not return any information. However, our roadmap contains more complex, flow chart-like workflows, where each action can result in information that can be passed to the following action, as well as serve as a basis to create different branches in your workflow

### Getting Started


```
docker run -p 8080:9010 -e WORKSPACE="/mnt"  -v c:\Users\%username%\tmpdata:/mnt ssischaefernl/savagerow
``` 

### Tutorials

### Contributor

- Vincent Beck (core contributor)
- Debarshi Basak

### Architecture

### Disclamer

This is not an official project from SSI Schaefer gmbh. This is an initiative from the team based out of the netherlands.

