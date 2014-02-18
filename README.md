#classycle-maven-plugin

Simple maven plugin for [classycle](http://classycle.sourceforge.net/)

Run

```
mvn classycle:analyse
```

to generate an xml report within the target directory.

and

```
mvn classycle:check
```

to check a project against a set of rules. 

Classycle rules can be supplied via a file specified with the dependencyDefinitionFile tag, or embedded in the pom e.g

```xml
<plugin>
	<groupId>org.pitest</groupId>
	<artifactId>classycle</artifactId>
	<version>0.4-SNAPSHOT</version>
	<executions>
		<execution>
			<id>verify</id>
			<phase>verify</phase>
			<goals>
				<goal>check</goal>
			</goals>
			<configuration>
				<dependencyDefinition>
					show allResults
					check absenceOfPackageCycles > 1 in com.example*
				</dependencyDefinition>

				<!-- This can be classycle.dependency.DefaultResultRenderer 
					for text (the default if omitted) or 
					classycle.dependency.XMLResultRenderer for xml -->
				<resultRenderer>classycle.dependency.DefaultResultRenderer</resultRenderer>
			</configuration>
		</execution>
	</executions>
</plugin>
```

If both a file and an embedded definition are supplied, the file will be used.

By default a violation will break the build - details of violations are written to checkresults.txt

Configuration options are supported based on the classycle ant task.

# Releases

## 0.4

* Support XML output
* Rename plugin to enable unqualified classycle:check syntax

## 0.3

* Support for Java 6.
* Support for `${project.reporting.outputEncoding}`

## 0.2

* Support for embedded dependency defintions.

## 0.1

* Initial release

		
