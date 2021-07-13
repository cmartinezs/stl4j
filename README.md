# STL4J: Simple Task Library 4 J
STL4J is **a simple task library for Java 11+** and gives you a base to be able to execute tasks in a simple way,
whether they are individual, group and/or chain.

![GitHub](https://img.shields.io/github/license/cmartinezs/stl4j)
![Hits](https://hitcounter.pythonanywhere.com/count/tag.svg?url=https://github.com/cmartinezs/stl4j)
![GitHub top language](https://img.shields.io/github/languages/top/cmartinezs/stl4j)
![GitHub repo file count](https://img.shields.io/github/directory-file-count/cmartinezs/stl4j)
![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/cmartinezs/stl4j)
![GitHub repo size](https://img.shields.io/github/repo-size/cmartinezs/stl4j)
![GitHub release (latest by date including pre-releases)](https://img.shields.io/github/downloads-pre/cmartinezs/stl4j/v0.1.0-alpha.2/total)
![GitHub issues](https://img.shields.io/github/issues/cmartinezs/stl4j)
![GitHub commit activity](https://img.shields.io/github/commit-activity/w/cmartinezs/stl4j)
![GitHub last commit](https://img.shields.io/github/last-commit/cmartinezs/stl4j)
![GitHub Release Date](https://img.shields.io/github/release-date/cmartinezs/stl4j)
![GitHub pull requests](https://img.shields.io/github/issues-pr/cmartinezs/stl4j)
![GitHub Discussions](https://img.shields.io/github/discussions/cmartinezs/stl4j)

![GitHub Repo stars](https://img.shields.io/github/stars/cmartinezs/stl4j?style=social)
![GitHub watchers](https://img.shields.io/github/watchers/cmartinezs/stl4j?style=social)
![GitHub forks](https://img.shields.io/github/forks/cmartinezs/stl4j?style=social)
## Installation
Use the package manager [**Maven**](https://maven.apache.org) to install **STL4J** via [**GitHub Packages**](https://jitpack.io/)
1. Add GitHub repository
```xml
<repositories>
    <repository>
        <id>github</id>
        <url>https://maven.pkg.github.com/cmartinezs/stl4j</url>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>
</repositories>
```
2. Add STL4J dependency
```xml
<dependency>
    <groupId>io.github.cmartinezs</groupId>
    <artifactId>stl4j</artifactId>
    <version>0.1.0-alpha.2</version>
</dependency>
```
3. *Optional*: You install and configure GPG for the signature of artifacts, or otherwise you do not want to sign, remove the associated plugin configuration in the [pom.xml](pom.xml)
```xml
<!-- Remove this plugin in case of not 
using signature in the artifacts -->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-gpg-plugin</artifactId>
    <version>1.6</version>
    <executions>
        <execution>
            <id>sign-artifacts</id>
            <phase>verify</phase>
            <goals>
                <goal>sign</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

4.- Using the command line
```bash
mvn clean install
```
## Usage
````java
// soon
````
## Documentation
Visit the [online documentation](https://cmartinezs.github.io/stl4j) for the most updated guide
## Contributing
Please read through our contributing guidelines. 
Included are directions for opening issues, coding standards, and notes on development.

Pull requests are welcome. 
For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.
## License

STL4J is an open source project by Carlos Martínez that is licensed under [GNU General Public License v3.0](LICENSE).
Carlos Martínez reserves the right to change the license of future releases.

|🟢Permissions|🟡Conditions|🔴Limitations|
|---|---|---|
|Commercial use|Disclose source|Liability|
|Distribution|License and copyright notice|Warranty|
|Modification|Same license||
|Patent use|State changes||
|Private use|||

## Public GPG key
Import my public gpg key for validate artifacts
```bash
> gpg --keyserver keys.openpgp.org --recv-keys B472CD84127EB71341FFC83D37015A09766E6088
```