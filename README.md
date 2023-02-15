# a Practice of Crawler

## Git
```
git checkout -b new_feature
git add .
git commit -m "wirte some describes of new feature"
git push --set-upstream origin new_feature
```

## Checkstyle 

```
<?xml version="1.0"?>
<!DOCTYPE module PUBLIC
        "-//Puppy Crawl//DTD Check Configuration 1.1//EN"
        "http://www.puppycrawl.com/dtds/configuration_1_1.dtd">
<module name="Checker">
    <property name="localeLanguage" value="en"/>
    <module name="NewlineAtEndOfFile">
        <property name="lineSeparator" value="lf" />
    </module>
    <module name="FileTabCharacter">
        <property name="fileExtensions" value="java,xml"/>
    </module>
    <module name="RegexpSingleline">
        <!-- \s matches whitespace character, $ matches end of line. -->
        <property name="format" value="\s+$"/>
        <property name="message" value="Line has trailing spaces."/>
    </module>
    <module name="TreeWalker">
        <module name="IllegalImport"/>
        <module name="RedundantImport"/>
        <module name="UnusedImports"/>
        <module name="NeedBraces"/>
        <module name="JavadocMethod">
            <property name="scope" value="public" />
        </module>
        <module name="ModifierOrder"/>
        <module name="RedundantModifier"/>
        <module name="UpperEll" />
        <module name="LeftCurly"/>
        <module name="NeedBraces"/>
        <module name="RightCurly"/>
        <module name="GenericWhitespace"/>
        <module name="WhitespaceAfter"/>
        <module name="NoWhitespaceBefore"/>
    </module>
</module>

```

