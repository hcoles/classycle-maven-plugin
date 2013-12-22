classycle-maven-plugin
======================

Simple maven plugin for [classycle](http://classycle.sourceforge.net/)

Run

```
mvn org.pitest:classycle:analyse
```

to generate an xml report within the target directory.

and

```
mvn org.pitest:classycle:check
```

to check a project against a set of rules. 

Classycle rules can be supplied via a file specified with the dependencyDefinitionFile tag, or embedded in the pom e.g

```
			<plugin>
			    <groupId>org.pitest</groupId>
			    <artifactId>classycle</artifactId>
			    <version>0.2</version>
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
			            </configuration>
				</execution>
			    </executions>
			</plugin>

```

If both a file and an embedded definition are supplied, the file will be used.

By default a violation will break the build - details of violations are written to checkresults.txt

Configuration options are supported based on the classycle ant task.

Releases
======================

0.2
===

Added support for embedded dependency defintions.

0.1
===

Initial release

		
