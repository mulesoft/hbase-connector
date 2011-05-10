Mule Hbase Cloud Connector
==========================

Mule Cloud connector to Hbase

Installation
------------

The connector can either be installed for all applications running within the Mule instance or can be setup to be used
for a single application.

*All Applications*

Download the connector from the link above and place the resulting jar file in
/lib/user directory of the Mule installation folder.

*Single Application*

To make the connector available only to single application then place it in the
lib directory of the application otherwise if using Maven to compile and deploy
your application the following can be done:

Add the connector's maven repo to your pom.xml:

    <repositories>
        <repository>
            <id>muleforge-releases</id>
            <name>MuleForge Snapshot Repository</name>
            <url>https://repository.muleforge.org/release/</url>
            <layout>default</layout>
        </repsitory>
    </repositories>

Add the connector as a dependency to your project. This can be done by adding
the following under the dependencies element in the pom.xml file of the
application:

    <dependency>
        <groupId>org.mule.modules</groupId>
        <artifactId>mule-module-hbase</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>

Configuration
-------------

You can configure the connector as follows:

    <hbase:config facade="value" properties="value"/>

Here is detailed list of all the configuration attributes:

| attribute | description | optional | default value |
|:-----------|:-----------|:---------|:--------------|
|name|Give a name to this configuration so it can be later referenced by config-ref.|yes||
|facade||yes|
|properties||yes|




Alive
-----

Answers if the HBase server is reachable

| attribute | description | optional | default value | possible values |
|:-----------|:-----------|:---------|:--------------|:----------------|
|config-ref|Specify which configuration to use for this invocation|yes||

Returns if the server can be reached and the master node is alive, false otherwise.



Create Table
------------

| attribute | description | optional | default value | possible values |
|:-----------|:-----------|:---------|:--------------|:----------------|
|config-ref|Specify which configuration to use for this invocation|yes||
|name||no||



Exists Table
------------

Answers if a given table exists, regardless it is enabled or not

| attribute | description | optional | default value | possible values |
|:-----------|:-----------|:---------|:--------------|:----------------|
|config-ref|Specify which configuration to use for this invocation|yes||
|name|the table name|no||

Returns only if the table exists, false otherwise



Delete Table
------------

| attribute | description | optional | default value | possible values |
|:-----------|:-----------|:---------|:--------------|:----------------|
|config-ref|Specify which configuration to use for this invocation|yes||
|name||no||



Is Enabled Table
----------------

Answers if the given table is enable You should check if the table exists
before calling this method. TODO why? Throws an exception?

| attribute | description | optional | default value | possible values |
|:-----------|:-----------|:---------|:--------------|:----------------|
|config-ref|Specify which configuration to use for this invocation|yes||
|name||no||

Returns only if the table was disabled. False otherwise



Enable Table
------------

| attribute | description | optional | default value | possible values |
|:-----------|:-----------|:---------|:--------------|:----------------|
|config-ref|Specify which configuration to use for this invocation|yes||
|name||no||



Disable Table
-------------

| attribute | description | optional | default value | possible values |
|:-----------|:-----------|:---------|:--------------|:----------------|
|config-ref|Specify which configuration to use for this invocation|yes||
|name||no||



Add Column
----------

| attribute | description | optional | default value | possible values |
|:-----------|:-----------|:---------|:--------------|:----------------|
|config-ref|Specify which configuration to use for this invocation|yes||
|tableName||no||
|columnFamilyName||no||
|maxVersions||yes||
|inMemory||yes||
|scope||yes||



Exists Column
-------------

| attribute | description | optional | default value | possible values |
|:-----------|:-----------|:---------|:--------------|:----------------|
|config-ref|Specify which configuration to use for this invocation|yes||
|tableName||no||
|columnFamilyName||no||



Modify Column
-------------

Changes a column family in a table

| attribute | description | optional | default value | possible values |
|:-----------|:-----------|:---------|:--------------|:----------------|
|config-ref|Specify which configuration to use for this invocation|yes||
|tableName|required|no||
|columnFamilyName|required|no||
|maxVersions||yes||
|blocksize||yes||
|compressionType||yes||
|compactionCompressionType||yes||
|inMemory||yes||
|timeToLive||yes||
|blockCacheEnabled||yes||
|bloomFilterType||yes||
|replicationScope||yes||
|values||yes||



Delete Column
-------------

| attribute | description | optional | default value | possible values |
|:-----------|:-----------|:---------|:--------------|:----------------|
|config-ref|Specify which configuration to use for this invocation|yes||
|tableName||no||
|columnFamilyName||no||



Get
---

| attribute | description | optional | default value | possible values |
|:-----------|:-----------|:---------|:--------------|:----------------|
|config-ref|Specify which configuration to use for this invocation|yes||
|tableName||no||
|row||no||
|maxVersions||yes||
|timestamp||yes||



Put
---

Saves the value at the specified cell (row + family:qualifier + timestamp)

| attribute | description | optional | default value | possible values |
|:-----------|:-----------|:---------|:--------------|:----------------|
|config-ref|Specify which configuration to use for this invocation|yes||
|tableName||no||
|row||no||
|columnFamilyName||no||
|columnQualifier||no||
|timestamp|the version of the cell TODO ??|yes||
|value||no||
|writeToWAL||yes||
|lock||yes||



Delete
------

Deletes a row

| attribute | description | optional | default value | possible values |
|:-----------|:-----------|:---------|:--------------|:----------------|
|config-ref|Specify which configuration to use for this invocation|yes||
|tableName||no||
|row||no||
|columnFamilyName|set null to delete all column families in the specified row|yes||
|columnQualifier|set null to delete all columns in the specified column family|yes||
|timestamp|set it to delete all versions of the specified column or column family with a timestamp less than or equal to the specified timestamp|yes||
|deleteAllVersions|set false to delete only the latest version of the specified column|yes||
|lock||yes||



Scan
----

Scans across all rows in a table. TODO and then?

| attribute | description | optional | default value | possible values |
|:-----------|:-----------|:---------|:--------------|:----------------|
|config-ref|Specify which configuration to use for this invocation|yes||
|tableName|limits the scan to a specific table. This is the only required argument.|no||
|columnFamilyName|limits the scan to a specific column family or null|yes||
|columnQualifier|limits the scan to a specific column or null. Requires a columnFamilyName to be defined.|yes||
|timestamp|limits the scan to a specific timestamp|yes||
|maxTimestamp|get versions of columns only within the specified timestamp range: [timestamp, maxTimestamp)|yes||
|caching|the number of rows for caching|yes||
|batch|the maximum number of values to return for each call to next() in the {@link ResultScanner}|yes||
|cacheBlocks|the number of rows for caching that will be passed to scanners|yes||
|maxVersions|limits the number of versions on each column|yes||
|allVersions|get all available versions on each column|yes||
|startRow|limits the beginning of the scan to the specified row inclusive|yes||
|stopRow|limits the end of the scan to the specified row exclusive|yes||



Increment
---------

Atomically increments a column value. If the column value does not yet exist
it is initialized to amount and written to the specified column.

| attribute | description | optional | default value | possible values |
|:-----------|:-----------|:---------|:--------------|:----------------|
|config-ref|Specify which configuration to use for this invocation|yes||
|tableName||no||
|row||no||
|columnFamilyName||no||
|columnQualifier||no||
|amount||no||
|writeToWAL|set it to false means that in a fail scenario, you will lose any increments that have not been flushed.|no||

Returns new value, post increment



Check And Put
-------------

Atomically checks if a row/family/qualifier value matches the expected value.
If it does, it adds the put.

| attribute | description | optional | default value | possible values |
|:-----------|:-----------|:---------|:--------------|:----------------|
|config-ref|Specify which configuration to use for this invocation|yes||
|tableName||no||
|row||no||
|checkColumnFamilyName||no||
|checkColumnQualifier||no||
|checkValue||no||
|putColumnFamilyName||no||
|putColumnQualifier||no||
|putTimestamp||yes||
|putValue||no||
|putWriteToWAL||yes||
|lock||yes||

Returns if the new put was executed, false otherwise



Check And Delete
----------------

Atomically checks if a row/family/qualifier value matches the expected value. 
If it does, it adds the delete.

| attribute | description | optional | default value | possible values |
|:-----------|:-----------|:---------|:--------------|:----------------|
|config-ref|Specify which configuration to use for this invocation|yes||
|tableName||no||
|row||no||
|checkColumnFamilyName||no||
|checkColumnQualifier||no||
|checkValue||no||
|deleteColumnFamilyName||no||
|deleteColumnQualifier||no||
|deleteTimestamp||yes||
|deleteAllVersions||yes||
|lock||yes||

Returns if the new delete was executed, false otherwise





































