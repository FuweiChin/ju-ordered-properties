# ju-ordered-properties
`java.util.Properties` ordered edition.

### Why ju-ordered-properties?
There is already an alternative of `java.util.Properties` [etiennestuder/java-ordered-properties](https://github.com/etiennestuder/java-ordered-properties), which is designed to use composition over inheritance, to keep entries ordered, while this one, [ju-ordered-properties](https://github.com/FuweiChin/ju-ordered-properties), prefers inheritance over composition.

### Usage
1. Checkout the project and install it to your local maven repository with `mvn install`.
2. Add a maven dependency to your pom.xml.
```
<dependency>
	<groupId>net.bldgos</groupId>
	<artifactId>ju-ordered-properties</artifactId>
	<version>0.1.0-SNAPSHOT</version>
</dependency>
```
3. Update your Java code, from something like  
```
Properties props=new Properties();
```
to
```
Properties props=new OrderedProperties();
```
4. Now you got ordered properties, try it and enjoy it.
