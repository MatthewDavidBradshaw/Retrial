# Retrial
A Gradle plugin that verifies dependency integrity at build time to guard against supply chain attacks.

Retrial was created to address the unresolved issues in Open Whisper Systems' [gradle-witness](https://github.com/signalapp/gradle-witness) plugin, and to provide a more convenient means of storing dependency checksums.

## Why use Retrial
Using remote dependencies is unavoidable for any non-trivial project, but doing so creates an opportunity for supply chain attacks. By acting as a MITM or by gaining direct access to a remote repository, an adversary could replace a legitimate dependency with their own compromised version. If used correctly, such an attack would allow an adversary to inject vulnerabilities into your project at build time.

Retrial guards against this problem by keeping a cryptographically secure checksum of each dependency, and comparing the saved checksums to the current checksums at build time. If any dependency has changed in any way, Retrial fails the build and describes the change. Since dependencies may have dependencies of their own, Retrial transitively checks the entire dependency graph to ensure integrity throughout.

## Usage
There are three steps to using the plugin:
1. Add the plugin to your project
2. Record the dependency checksums
3. Verify the dependency checksums

### Add the plugin to your project
Retrial cannot be published as an artifact because doing so would create a bootstrap problem. Instead, it must be built from the source and included in your project as a jar.

Start by cloning the repository:
```shell
git clone https://github.com/matthewtamlin/retrial.git
```

Then build the plugin using Gradle:
```shell
cd retrial

# On MacOS and Linux
./gradlew buildRelease

# On Windows
gradlew buildRelease
```

Next copy the jar from `retrial/build/libs/retrial.jar` to the `libs` folder of your project.
```shell
cp build/libs/retrial.jar yourproject/app/libs/retrial.jar

# Or just use Finder/explorer to copy the files...
```

Finally add the following to your Gradle build file:
```gradle
buildscript {
  dependencies {
    classpath files('libs/retrial.jar')
  }
}

apply plugin: 'retrial'

```

That’s it! You've successfully added Retrial to your project.

### Recording the dependency checksums
To create the checksum record, run the `recordDependencyChecksums` task:
```
# On MacOS/Linux
./gradlew recordDependencyChecksums

# On Windows
gradlew recordDependencyChecksums
```

This task creates the `retrial-checksums.json` file in your project directory and writes the checksums to it. You can generally disregard this file, but make sure its checked in to source control and avoid manually editing it.

Whenever you intentionally update/add/remove a dependency, you’ll need to run the record dependencies task again to update the record.

### Verify the dependency checksums
To verify your dependencies against the record, run the `verifyDependencyChecksums` task:
```
# On MacOS/Linux
./gradlew verifyDependencyChecksums

# On Windows
gradlew verifyDependencyChecksums
```

This task compares the saved checksums against the current checksums and fails the build if:
- There are any additional dependency in the build that are missing from the record.
- There are any additional dependency in the record that are missing from the build.
- There are any checksum mismatches.

By default, the task only runs when manually invoked. To automatically run the task every time the project is built, add the following to your gradle build file:
```
build.finalizedBy(verifyDependencyChecksums)
```

Retrial is very lightweight so you shouldn’t notice any effect on your build times.

## Limitations
Its important to recognise that Retrial doesn't provide any assurance that your dependencies are actually free from vulnerabilities. All it does is ensure that the remote dependencies haven’t changed since you added them. Retrial will not save you if you include a dependency that already has a vulnerability and then record the checksums. Depending on your circumstances and the acceptable level of risk, you may want to perform a full audit of your dependencies prior to using Retrial.

## What about Gradle dependency locking?
Gradle has built in support for dependency locking, but it doesn't offer any protection against supply chain attacks. Dependency locking makes builds reproducable when using dynamic versioning, but it trusts the version declared by the repository and never performs any kind of integrity check. As such, dependency locking and Retrial serve entirely different purposes. 

Retrial and dependency locking can be used at the same time with no conflicts. In fact, you probably want to use dependency locking if you’re using Retrial and dynamic dependency ranges, or else your build may fail spontaneously.

## Contact
This repository is owned and maintained by Matt Tamlin. Feel free to get in contact at any time via [email](mailto:matthew.tamlin@icloud.com or [twitter](https://twitter.com/tamlinmatthew).

## Contributing
If you wish to contribute, please read the [contributing guidelines](CONTRIBUTING.md).