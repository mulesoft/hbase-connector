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
|properties|HBase internal configuration properties. Consult HBase documentation.|yes|




Is Alive Server
---------------

Answers if the HBase server is reachable

| attribute | description | optional | default value | possible values |
|:-----------|:-----------|:---------|:--------------|:----------------|
|config-ref|Specify which configuration to use for this invocation|yes||

Returns if the server can be reached and the master node is alive, false otherwise.



Create Table
------------

Creates a new table given its name. The descriptor must be unique and not
reserved.

| attribute | description | optional | default value | possible values |
|:-----------|:-----------|:---------|:--------------|:----------------|
|config-ref|Specify which configuration to use for this invocation|yes||
|name|the descriptor for the new table.|no||



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

Disables and deletes an existent table.

| attribute | description | optional | default value | possible values |
|:-----------|:-----------|:---------|:--------------|:----------------|
|config-ref|Specify which configuration to use for this invocation|yes||
|name|name of table to delete|no||



Is Enabled Table
----------------

Answers if the given existent table is enabled.

| attribute | description | optional | default value | possible values |
|:-----------|:-----------|:---------|:--------------|:----------------|
|config-ref|Specify which configuration to use for this invocation|yes||
|name|name of the table to query for its enabling state|no||

Returns only if the table was disabled. False otherwise



Enable Table
------------

Enables an existent table.

| attribute | description | optional | default value | possible values |
|:-----------|:-----------|:---------|:--------------|:----------------|
|config-ref|Specify which configuration to use for this invocation|yes||
|name|name of the table to enable|no||



Disable Table
-------------

Disables an existent table

| attribute | description | optional | default value | possible values |
|:-----------|:-----------|:---------|:--------------|:----------------|
|config-ref|Specify which configuration to use for this invocation|yes||
|name|the table name to disable|no||



Add Column
----------

Adds a column family to a table given a table and column name. This operation
gracefully handles necessary table disabling and enabled.

| attribute | description | optional | default value | possible values |
|:-----------|:-----------|:---------|:--------------|:----------------|
|config-ref|Specify which configuration to use for this invocation|yes||
|tableName|the name of the target table|no||
|columnFamilyName|the name of the column|no||
|maxVersions|the optional maximum number of versions the column family supports|yes||
|inMemory|if all the column values will be stored in the region's cache|yes|false|
|scope||yes||



Exists Column
-------------

Answers if column family exists.

| attribute | description | optional | default value | possible values |
|:-----------|:-----------|:---------|:--------------|:----------------|
|config-ref|Specify which configuration to use for this invocation|yes||
|tableName|the target table name|no||
|columnFamilyName|the target column family name|no||

Returns if the column exists, false otherwise



Modify Column
-------------

Changes one or more properties of a column family in a table. This operation
gracefully handles necessary table disabling and enabled.

| attribute | description | optional | default value | possible values |
|:-----------|:-----------|:---------|:--------------|:----------------|
|config-ref|Specify which configuration to use for this invocation|yes||
|tableName|required the target table|no||
|columnFamilyName|required the target column family|no||
|maxVersions|the new max amount of versions|yes||
|blocksize|the the new block size|yes||
|compressionType|the new compression type|yes||*LZO*, *GZ*, *NONE*, *algorithm*
|compactionCompressionType|the new compaction compression type|yes||*LZO*, *GZ*, *NONE*, *algorithm*
|inMemory|new value for if values are stored in Region's cache|yes||
|timeToLive|new ttl|yes||
|blockCacheEnabled|new value of enabling block cache|yes||
|bloomFilterType|new value of bloom filter type|yes||*NONE*, *ROW*, *ROWCOL*, *bloomType*
|replicationScope||yes||
|values||yes||



Delete Column
-------------

| attribute | description | optional | default value | possible values |
|:-----------|:-----------|:---------|:--------------|:----------------|
|config-ref|Specify which configuration to use for this invocation|yes||
|tableName||no||
|columnFamilyName||no||



Get Values
----------

Answers the values at the given row - (table, row) combination

| attribute | description | optional | default value | possible values |
|:-----------|:-----------|:---------|:--------------|:----------------|
|config-ref|Specify which configuration to use for this invocation|yes||
|tableName||no||
|row||no||
|maxVersions||yes||
|timestamp||yes||

Returns result



Put Value
---------

Saves a value at the specified (table, row, familyName, familyQualifier,
timestamp) combination

| attribute | description | optional | default value | possible values |
|:-----------|:-----------|:---------|:--------------|:----------------|
|config-ref|Specify which configuration to use for this invocation|yes||
|tableName||no||
|row||no||
|columnFamilyName|the column family dimension|no||
|columnQualifier|the column qualifier dimension|no||
|timestamp|the version dimension|yes||
|value|the value to put. It must be either a byte array or a serializable object. As a special case, strings are saved always in standard utf-8 format.|no||
|writeToWAL||yes||
|lock||yes||



Delete Values
-------------

Deletes the values at a given row

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



Scan Table
----------

Scans across all rows in a table, returning a scanner over it

| attribute | description | optional | default value | possible values |
|:-----------|:-----------|:---------|:--------------|:----------------|
|config-ref|Specify which configuration to use for this invocation|yes||
|tableName|limits the scan to a specific table. This is the only required argument.|no||
|columnFamilyName|limits the scan to a specific column family or null|yes||
|columnQualifier|limits the scan to a specific column or null. Requires a columnFamilyName to be defined.|yes||
|timestamp|limits the scan to a specific timestamp|yes||
|maxTimestamp|get versions of columns only within the specified timestamp range: [timestamp, maxTimestamp)|yes||
|caching|the number of rows for caching|yes||
|batch|the maximum number of values to return for each call to next() in the ResultScanner|yes||
|cacheBlocks|the number of rows for caching that will be passed to scanners|yes||
|maxVersions|limits the number of versions on each column|yes||
|allVersions|get all available versions on each column|yes||
|startRow|limits the beginning of the scan to the specified row inclusive|yes||
|stopRow|limits the end of the scan to the specified row exclusive|yes||

Returns scanner over the table



Increment Value
---------------

Atomically increments the value of at a (table, row, familyName,
familyQualifier) combination. If the cell value does not yet exist it is
initialized to amount.

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



Check And Put Value
-------------------

Atomically checks if a value at a (table, row,family,qualifier) matches the
given one. If it does, it performs the put.

| attribute | description | optional | default value | possible values |
|:-----------|:-----------|:---------|:--------------|:----------------|
|config-ref|Specify which configuration to use for this invocation|yes||
|tableName||no||
|row||no||
|checkColumnFamilyName||no||
|checkColumnQualifier||no||
|checkValue|the value to check. It must be either a byte array or a serializable object. As a special case, strings are saved always in standard utf-8 format.|no||
|putColumnFamilyName||no||
|putColumnQualifier||no||
|putTimestamp||yes||
|value|the value to put. It must be either a byte array or a serializable object. As a special case, strings are saved always in standard utf-8 format.|no||
|putWriteToWAL||yes||
|lock||yes||

Returns if the new put was executed, false otherwise



Check And Delete Value
----------------------

Atomically checks if a value at a (table, row,family,qualifier) matches the
given one. If it does, it performs the delete.

| attribute | description | optional | default value | possible values |
|:-----------|:-----------|:---------|:--------------|:----------------|
|config-ref|Specify which configuration to use for this invocation|yes||
|tableName||no||
|row||no||
|checkColumnFamilyName||no||
|checkColumnQualifier||no||
|checkValue|the value to check. It must be either a byte array or a serializable object. As a special case, strings are saved always in standard utf-8 format.|no||
|deleteColumnFamilyName||no||
|deleteColumnQualifier||no||
|deleteTimestamp||yes||
|deleteAllVersions||yes||
|lock||yes||

Returns if the new delete was executed, false otherwise





































