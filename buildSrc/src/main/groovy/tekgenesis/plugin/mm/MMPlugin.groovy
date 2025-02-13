package tekgenesis.plugin.mm

import groovy.io.FileType
import org.gradle.api.InvalidUserDataException
import org.gradle.api.Plugin;
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ConfigurationContainer
import org.gradle.api.logging.LogLevel
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.bundling.Jar

/**
 * MetaModel Plugin.
 * To debug, use: `export DEBUG_MMBUILD=true`
 */
class MMPlugin implements Plugin<Project> {
    void apply(Project project) {
        def mainMMDir = new File(project.projectDir, "src/main/mm")
        def testMMDir = new File(project.projectDir, "src/test/mm")

        ConfigurationContainer configurations = project.getConfigurations();
        JavaPluginConvention javaConvention = project.getConvention().getPlugin(JavaPluginConvention.class);


        Configuration mmConfiguration = configurations.create("mm");


        if (mainMMDir.exists()) {
            project.task('mm') {
                setOutputs('main', it)
                setInputs(it, mainMMDir.absolutePath)
                doLast {
                    generate(project, mainMMDir, 'main')
                }
            }
            project.task('mmServices') {
                it.outputs.dir(new File(getGenDir(it.project, 'services')))
                setInputs(it, mainMMDir.absolutePath)
                doLast {
                    generate(project, mainMMDir, 'services', '-r')
                }
            }

            project.task('mmModels') {
                it.outputs.dir(new File(getOutputDir(it.project, 'metamodels')))
                setInputs(it, mainMMDir.absolutePath)
                doLast {
                    generate(project, mainMMDir, 'metamodels', '-e')
                }

            }

            project.task('mmDoc') {
                it.outputs.dir(new File(getDocOutputDir(it.project)))
                setInputs(it, mainMMDir.absolutePath)
                doLast {
                    generateDoc(project, mainMMDir)
                }
            }

            def sourceSets = javaConvention.getSourceSets()
            SourceSet services = sourceSets.create("services")

            services.java.srcDir project.projectDir.absolutePath + "/src_managed/services/mm"
            services.setCompileClasspath(project.sourceSets.main.compileClasspath)


            SourceSet metamodels = sourceSets.create("metamodels")

            metamodels.java.srcDir project.projectDir.absolutePath + "/src_managed/metamodels/mm"
            metamodels.setCompileClasspath(project.sourceSets.main.compileClasspath)

            Jar servicesJar = project.getTasks().create("servicesJar", Jar.class);
            servicesJar.from(services.getOutput())
            servicesJar.setClassifier('services')
            servicesJar.setDestinationDir(new File(project.buildDir.absolutePath + "/services"))
            servicesJar.dependsOn(project.compileServicesJava)

            Jar entitiesJar = project.getTasks().create("metamodelsJar", Jar.class);
            entitiesJar.from(getOutputDir(project, 'metamodels'))
            entitiesJar.setClassifier('metamodels')
            entitiesJar.setDestinationDir(new File(project.buildDir.absolutePath + "/metamodels"))
            entitiesJar.dependsOn(project.compileMetamodelsJava)

            project.compileServicesJava.dependsOn(project.mmServices)
            project.compileMetamodelsJava.dependsOn(project.mmModels)



            Jar servicesSrcJar = project.getTasks().create("servicesSourcesJar", Jar.class);
            servicesSrcJar.from(services.getAllSource())
            servicesSrcJar.setClassifier('services-src')


            project.compileJava.dependsOn(project.mm)
            project.sourceSets.main.java.srcDir project.projectDir.absolutePath + "/src_managed/main/mm"


            final Configuration configuration = project.getConfigurations().getByName('mm');
            project.mm.dependsOn(configuration.getTaskDependencyFromProjectDependency(true, 'jar'));

            project.task('cleanMM') {
                doLast {
                    new File(project.projectDir.absolutePath + "/src_managed").deleteDir()
                }
            }
        }


        if (testMMDir.exists()) {
            project.task('mmTest') {
                setOutputs('test', it)
                setInputs(it, testMMDir.absolutePath)
                doLast {
                    generate(project, testMMDir, 'test')
                }
            }
            project.compileTestJava.dependsOn(project.mmTest)
            project.sourceSets.test.java.srcDir project.projectDir.absolutePath + "/src_managed/test/mm"

            project.task('cleanMM') {
                doLast{
                    new File(project.projectDir.absolutePath + "/src_managed").deleteDir()
                }
            }

        }
//        project.idea.module.iml.withXml {
//            def node = it.asNode()
//            node.appendNode('iLoveGradle', 'true')
//            node.appendNode('butAlso', 'I find increasing pleasure tinkering with output *.iml content. Yeah!!!')
//        }

    }


    def static setOutputs(String scope, Task task) {

        task.outputs.dir(new File(getGenDir(task.project, scope)))
        task.outputs.dir(task.project.buildDir.absolutePath + "/classes/"+scope+"/META-INF")
        task.outputs.dir(task.project.buildDir.absolutePath + "/classes/"+scope+"/db")
        task.outputs.dir(task.project.buildDir.absolutePath + "/resources/" + scope + "/db")
    }

    def static setInputs(Task task, String srcDir) {
        task.inputs.files(task.project.fileTree(dir: srcDir, include: "**/*.mm").files)
    }

    static def generate(Project project, File src, String scope, String type = '') {
        def sourceDir = project.projectDir.absolutePath + "/src/"+(scope == 'services' ? 'main' : scope)+"/java"
        def list = []
        src.eachFileRecurse (FileType.FILES) { file ->
            if(file.getName().endsWith(".mm")) list << file.absolutePath
        }

        def mms = list.join(" ")
        project.logger.debug("mms "+mms)

        def mmBuildFile = new File(project.rootProject.projectDir, "../../tools/bin/mmbuild")
	if(!mmBuildFile.exists()) mmBuildFile = new File(project.rootProject.projectDir, "../tools/bin/mmbuild")
        def mmCmd = mmBuildFile.exists() ? mmBuildFile.absolutePath : "mmbuild"
        def cmd = mmCmd + " -cp " + (scope == 'test' ? project.compileTestJava.classpath.asPath : project.compileJava.classpath.asPath) +" "+ type  + " -p -o " + getOutputDir(project, scope) + " -g " + getGenDir(project, scope) + " -s " + sourceDir + " -m " + src.absolutePath + " " + mms
        project.logger.log(LogLevel.INFO, "Building mm")
        project.logger.log(LogLevel.DEBUG, "executing command: "+cmd)
        def output = new StringBuffer()
        Process result = Runtime.getRuntime().exec(cmd)
        result.consumeProcessOutput(output, output)
        result.waitFor()
        if (result.exitValue() == 0) {
            project.logger.log(LogLevel.INFO, output.toString())
        } else {
            throw new InvalidUserDataException(output.toString())
        }
    }

    def static getOutputDir(Project project, String scope) {
        return project.buildDir.absolutePath + "/classes/" + scope
    }
def static getGenDir(Project project, String scope) {
    return project.projectDir.absolutePath + "/src_managed/"+ scope + "/mm"
}
    def static getDocOutputDir(Project project) {
        return project.rootProject.projectDir.absolutePath+ "/mmdoc/"
    }

    static def generateDoc(Project project, File src) {
        def list = []
        src.eachFileRecurse(FileType.FILES) { file ->
            if (file.getName().endsWith(".mm")) list << file.absolutePath
        }

        def mms = list.join(" ")
        project.logger.debug("mms " + mms)
        def classpath = project.compileJava.classpath.asPath

        if (!classpath.contains("codegen-metamodel.jar")) {
            throw new InvalidUserDataException("Sui Generis distribution not found at " + project.rootProject.projectDir.absolutePath + "/suigeneris")
        }

        def cmd = "java " + (Boolean.getBoolean("debugMM") ? "-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005" : "") + " -classpath " + classpath +
                " tekgenesis.codegen.documentation.MMDoc" +" -o " + getDocOutputDir(project) + " -n " + project.name + " -m " + src.absolutePath + " " + mms
        def output = new StringBuffer()
        Process result = Runtime.getRuntime().exec(cmd)
        result.consumeProcessOutput(output, output)
        result.waitFor()
        if (result.exitValue() == 0) {
            project.logger.log(LogLevel.INFO, output.toString())
        } else {
            throw new InvalidUserDataException(output.toString())
        }
    }

}
