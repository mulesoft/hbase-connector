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

| attribute | description | optional | default value | possible values |
|:-----------|:-----------|:---------|:--------------|:----------------|
|config-ref|Specify which configuration to use for this invocation|yes||



Create Table
------------

| attribute | description | optional | default value | possible values |
|:-----------|:-----------|:---------|:--------------|:----------------|
|config-ref|Specify which configuration to use for this invocation|yes||
|name||no||



Exists Table
------------

| attribute | description | optional | default value | possible values |
|:-----------|:-----------|:---------|:--------------|:----------------|
|config-ref|Specify which configuration to use for this invocation|yes||
|name||no||



Delete Table
------------

| attribute | description | optional | default value | possible values |
|:-----------|:-----------|:---------|:--------------|:----------------|
|config-ref|Specify which configuration to use for this invocation|yes||
|name||no||



Is Enabled Table
----------------

| attribute | description | optional | default value | possible values |
|:-----------|:-----------|:---------|:--------------|:----------------|
|config-ref|Specify which configuration to use for this invocation|yes||
|name||no||



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

| attribute | description | optional | default value | possible values |
|:-----------|:-----------|:---------|:--------------|:----------------|
|config-ref|Specify which configuration to use for this invocation|yes||
|tableName||no||
|columnFamilyName||no||
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

| attribute | description | optional | default value | possible values |
|:-----------|:-----------|:---------|:--------------|:----------------|
|config-ref|Specify which configuration to use for this invocation|yes||
|tableName||no||
|row||no||
|columnFamilyName||no||
|columnQualifier||no||
|timestamp||yes||
|value||no||
|writeToWAL||yes||
|lock||yes||



Delete
------

| attribute | description | optional | default value | possible values |
|:-----------|:-----------|:---------|:--------------|:----------------|
|config-ref|Specify which configuration to use for this invocation|yes||
|tableName||no||
|row||no||
|columnFamilyName||yes||
|columnQualifier||yes||
|timestamp||yes||
|deleteAllVersions||yes||
|lock||yes||



Scan
----

| attribute | description | optional | default value | possible values |
|:-----------|:-----------|:---------|:--------------|:----------------|
|config-ref|Specify which configuration to use for this invocation|yes||
|tableName||no||
|columnFamilyName||yes||
|columnQualifier||yes||
|timestamp||yes||
|maxTimestamp||yes||
|caching||yes||
|batch||yes||
|cacheBlocks||yes||
|maxVersions||yes||
|allVersions||yes||
|startRow||yes||
|stopRow||yes||



Increment
---------

| attribute | description | optional | default value | possible values |
|:-----------|:-----------|:---------|:--------------|:----------------|
|config-ref|Specify which configuration to use for this invocation|yes||
|tableName||no||
|row||no||
|columnFamilyName||no||
|columnQualifier||no||
|amount||no||
|writeToWAL||no||



Check And Put
-------------

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



Check And Delete
----------------

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





































