package net.serenitybdd.filterchain.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class SerenityFilterchainPlugin implements Plugin<Project> {

    File reportDirectory
    File historyDirectory

    @Override
    void apply(Project project) {
        project.extensions.create("serenityFilterchain", FilterChainConfig)
        project.task('chain') {
            group 'Serenity BDD Filterchain'
            description 'Generates complex aggregated Serenity reports'
            doLast {
                println project.serenityFilterchain.outputs.size()

                def outputs = project.serenityFilterchain.buildLinks()
                outputs.each{it.clean()}
                outputs.each{it.write()}
            }
        }
    }
}
