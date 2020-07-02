# frdp-framework

ForgeRock Demonstration Platform Java framework.  Contains Java packages that are used by other projects.

`git clone https://github.com/ForgeRock/frdp-framework.git`

# Disclaimer

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

# License

[MIT](/LICENSE)

## Requirements

The following items must be installed:

1. [Apache Maven](https://maven.apache.org/)
1. [Java Development Kit 8](https://openjdk.java.net/)

## Build

Run "maven" processes to clean, compile, install the jar package:

```bash
mvn clean compile install
```

Packages are added to the user's home folder:

```bash
find ~/.m2/repository/com/forgerock/frdp/frdp-framework
/home/forgerock/.m2/repository/com/forgerock/frdp/frdp-framework
/home/forgerock/.m2/repository/com/forgerock/frdp/frdp-framework/maven-metadata-local.xml
/home/forgerock/.m2/repository/com/forgerock/frdp/frdp-framework/1.1.0
/home/forgerock/.m2/repository/com/forgerock/frdp/frdp-framework/1.1.0/frdp-framework-1.1.0.pom
/home/forgerock/.m2/repository/com/forgerock/frdp/frdp-framework/1.1.0/_remote.repositories
/home/forgerock/.m2/repository/com/forgerock/frdp/frdp-framework/1.1.0/maven-metadata-local.xml
/home/forgerock/.m2/repository/com/forgerock/frdp/frdp-framework/1.1.0/frdp-framework-1.1.0.jar
```
