package net.serenitybdd.filterchain.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class SerenityFilterChainPlugin implements Plugin<Project> {

    File reportDirectory
    File historyDirectory

    @Override
    void apply(Project project) {
        project.extensions.create("serenityFilterchain", FilterChainConfig,project)
        project.task('chain') {
            group 'Serenity BDD Filterchain'
            description 'Generates complex aggregated Serenity reports'
            doLast {
                println project.serenityFilterchain.outputs.size()

                def outputs = project.serenityFilterchain.buildLinks()
                outputs.each{it.clean()}
                outputs.each{it.write()}
                if(project.serenityFilterchain.publishingLinks !=null){
                    project.serenityFilterchain.publishingLinks.each{it.publish()}
                }
            }
        }
    }
}
