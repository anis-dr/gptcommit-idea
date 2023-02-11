package com.github.anis00723.gptcommitidea.services

import com.intellij.openapi.project.Project
import com.github.anis00723.gptcommitidea.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))

        System.getenv("CI")
            ?: TODO("Don't forget to remove all non-needed sample code files with their corresponding registration entries in `plugin.xml`.")
    }

    /**
     * Chosen by fair dice roll, guaranteed to be random.
     */
    fun getRandomNumber() = 4
}
