package com.github.ebina4yaka.volumioplayer.toolWindow

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import com.intellij.util.concurrency.AppExecutorUtil
import java.util.concurrent.TimeUnit

class MyToolWindowFactory : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val myToolWindow = VolumoPlayerToolWindow(toolWindow)
        val content = ContentFactory.getInstance().createContent(myToolWindow.setContent(), null, false)
        toolWindow.contentManager.addContent(content)

        AppExecutorUtil.getAppScheduledExecutorService().scheduleWithFixedDelay({
            ApplicationManager.getApplication().invokeLater {
                myToolWindow.updateContent()
            }
        }, 0, 1, TimeUnit.SECONDS)
    }
}