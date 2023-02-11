package com.github.anis00723.gptcommitidea.actions

import com.github.difflib.DiffUtils
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vcs.ProjectLevelVcsManager
import com.intellij.openapi.vcs.changes.Change
import com.intellij.openapi.vcs.changes.ChangeListManager
import com.intellij.openapi.vfs.VirtualFile
import com.jetbrains.rd.framework.base.deepClonePolymorphic
import com.jetbrains.rd.util.string.printToString
import org.apache.maven.model.Repository
import java.io.BufferedReader
import java.io.InputStreamReader


class GPTCommitAction : AnAction("Generate Commit Message") {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val vcsManager = ProjectLevelVcsManager.getInstance(project)
        val roots = vcsManager.allVcsRoots
        if (roots.isEmpty()) {
            Messages.showErrorDialog("No repository found", "Error")
            return
        }

        if (roots.size > 1) {
            Messages.showMessageDialog(
                    project,
                    "Multiple repositories found. Using the first one.",
                    "Warning",
                    Messages.getWarningIcon()
            )
        }

        val changeListManager = ChangeListManager.getInstance(project)
        val changes: List<Change> = changeListManager.allChanges.toList()

        for (change in changes) {
            val beforeContent = change.beforeRevision?.content ?: ""
            val afterContent = change.afterRevision?.content ?: ""
            val diff = DiffUtils.diff(beforeContent.split("\n"), afterContent.split("\n"))
            val diffStringBuilder = StringBuilder()
            for (d in diff.deltas) {
                diffStringBuilder.append("--- ${d.source.position + 1} ${d.source.position + d.source.lines.size}\n")
                diffStringBuilder.append("+++ ${d.target.position + 1} ${d.target.position + d.target.lines.size}\n")
                for (originalLine in d.source.lines) {
                    diffStringBuilder.append("- $originalLine\n")
                }
                for (revisedLine in d.target.lines) {
                    diffStringBuilder.append("+ $revisedLine\n")
                }
            }
            println(diffStringBuilder.toString())
        }

//        // Get the commit message here
//        val commitMessage = "TODO: Replace this with code that generates the commit message"

        // Set the commit message for the repository
//        repo.inputBox.value = commitMessage
    }
}

fun runGitDiffCommand(): String {
    val process = Runtime.getRuntime().exec("git diff")
    val reader = BufferedReader(InputStreamReader(process.inputStream))

    val output = StringBuilder()
    var line = reader.readLine()
    while (line != null) {
        output.append(line).append("\n")
        line = reader.readLine()
    }

    return output.toString()
}