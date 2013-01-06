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

to check a project against a set of rules. By default a violation will break the build - details of violations
are written to checkresults.txt

Configuration options are supported based on the classycle ant task.
		
