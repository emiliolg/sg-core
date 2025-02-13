package tekgenesis.plugin.ix

import org.gradle.api.InvalidUserDataException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.logging.LogLevel

class IxPlugin implements Plugin<Project> {
    void apply(Project project) {
        def mainMMDir = new File(project.projectDir, "src/main/mm")
        def testMMDir = new File(project.projectDir, "src/test/mm")



        if (mainMMDir.exists()) {
            project.task('ix') {
                setOutputs('main', it)
                setInputs(it, mainMMDir.absolutePath)
                doLast {
                    generate(project, mainMMDir, 'main')
                }
            }
            project.compileJava.dependsOn(project.ix)
            project.sourceSets.main.java.srcDir project.projectDir.absolutePath + "/src_managed/main/mm"

            project.task('cleanIx') {
                new File(project.projectDir.absolutePath + "/src_managed").deleteDir()
            }
        }


        if (testMMDir.exists()) {
            project.task('ixTest') {
                setOutputs('test', it)
                setInputs(it, testMMDir.absolutePath)
                doLast {
                    generate(project, testMMDir, 'test')
                }
            }
            project.compileTestJava.dependsOn(project.ixTest)
            project.sourceSets.test.java.srcDir project.projectDir.absolutePath + "/src_managed/test/mm"

            project.task('cleanIx') {
                new File(project.projectDir.absolutePath + "/src_managed").deleteDir()
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
    }

    def static setInputs(Task task, String srcDir) {
        task.inputs.files(task.project.fileTree(dir: srcDir, include: "**/*.sc").files)
    }

    static def generate(Project project, File src, String scope) {
        def sourceDir = project.projectDir.absolutePath + "/src/"+scope+"/java"
        def sources = project.fileTree(dir: src, include: "**/*.sc")
        def mms = sources.inject([]) { result, entry ->
            result << entry.getAbsolutePath()
        }.join(" ")
        def mmBuildFile = new File(project.rootProject.projectDir, "../../tools/bin/ixbuild")
	if (!mmBuildFile.exists()) mmBuildFile = new File(project.rootProject.projectDir, "../tools/bin/ixbuild")
        def mmCmd = mmBuildFile.exists() ? mmBuildFile.absolutePath : "ixbuild"
        def cmd = mmCmd + " -cp " + (scope == 'test' ? project.compileTestJava.classpath.asPath : project.compileJava.classpath.asPath)+ " -p -o " + getOutputDir(project, scope) + " -g " + getGenDir(project, scope) + " -s " + sourceDir + " -m " + src.absolutePath + " " + mms
        project.logger.log(LogLevel.INFO, "Building mm")

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

}
