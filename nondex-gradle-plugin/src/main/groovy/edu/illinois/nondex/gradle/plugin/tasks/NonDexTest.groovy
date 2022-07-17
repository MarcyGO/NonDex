package edu.illinois.nondex.gradle.plugin.tasks

import edu.illinois.nondex.common.ConfigurationDefaults
import edu.illinois.nondex.common.Utils
import edu.illinois.nondex.instr.Main
import org.gradle.api.artifacts.Configuration
import org.gradle.api.tasks.testing.Test

class NonDexTest extends Test {
    static final String NAME = "nondexTest"

    void init() {
        setDescription("Test with NonDex")
        setGroup("NonDex")

        testLogging {
            exceptionFormat 'full'
        }

        doFirst {
            String commonPath = project.buildscript.configurations.classpath.find {it.name.startsWith("nondex-common")}.absolutePath
            String outPath = project.buildDir.absolutePath + File.separator + "out.jar"

            Main.main(outPath)

            def args = "-Xbootclasspath/p:" + outPath + File.pathSeparator + commonPath
            jvmArgs args, "-D" + ConfigurationDefaults.PROPERTY_EXECUTION_ID + "=" + Utils.getFreshExecutionId()
            readInputArgs()
            println "Running with arguments: " + getJvmArgs()
        }
    }

    void readInputArgs() {
        putProperty(ConfigurationDefaults.PROPERTY_SEED, ConfigurationDefaults.DEFAULT_SEED_STR)
        putProperty(ConfigurationDefaults.PROPERTY_MODE, ConfigurationDefaults.DEFAULT_MODE_STR)
        putProperty(ConfigurationDefaults.PROPERTY_FILTER, ConfigurationDefaults.DEFAULT_FILTER)
        putProperty(ConfigurationDefaults.PROPERTY_START, ConfigurationDefaults.DEFAULT_START_STR)
        putProperty(ConfigurationDefaults.PROPERTY_END, ConfigurationDefaults.DEFAULT_END_STR)
    }

    void putProperty(String property_name, String default_val) {
        jvmArgs "-D" + property + "=" + System.getProperty(property_name, default_val)
    }
}
