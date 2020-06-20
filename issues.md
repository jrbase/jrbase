## ISSUE
### issue1
```
> Task :client:test FAILED
Error occurred during initialization of VM
java.lang.InternalError: Could not create SecurityManager: worker.org.gradle.process.internal.worker.child.BootstrapSecurityManager
        at sun.misc.Launcher.<init>(Launcher.java:103)
        at sun.misc.Launcher.<clinit>(Launcher.java:54)
        at java.lang.ClassLoader.initSystemClassLoader(ClassLoader.java:1451)
        at java.lang.ClassLoader.getSystemClassLoader(ClassLoader.java:1436)


FAILURE: Build failed with an exception.

* What went wrong:
Execution failed for task ':client:test'.
> Process 'Gradle Test Executor 3' finished with non-zero exit value 1

* Try:
Run with --stacktrace option to get the stack trace. Run with --info or --debug option to get more log output. Run with --scan to get full insights.

* Get more help at https://help.gradle.org

BUILD FAILED in 6s
8 actionable tasks: 8 executed

```
### solve1
```
https://github.com/gradle/gradle/issues/4689
rm -rf .gradle/
```

