package com.github.ebina4yaka.volumioplayer.toolWindow

import com.github.ebina4yaka.volumioplayer.services.VolumioService
import com.intellij.openapi.wm.ToolWindow
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPanel
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Image
import java.net.URI
import javax.swing.BorderFactory
import javax.swing.ImageIcon

class VolumoPlayerToolWindow(private val toolWindow: ToolWindow)  {
    // TODO: 設定画面を追加してそこからとる
    private val volumioHost = "http://firefly.local"
    private val service = VolumioService(volumioHost)

    fun updateContent() {
        val state = service.fetchState()
        val imageUri = URI(state.albumart)
        val icon = ImageIcon(imageUri.toURL())
        val newAlbumartLabel = JBLabel(scaleImage(icon, 250, 250))

        val toolWindowContent = toolWindow.contentManager.contents[0].component

        var componentIndex = 0

        val albumartLabel = (toolWindowContent as JBPanel<*>).components[componentIndex++] as JBLabel
        albumartLabel.icon = newAlbumartLabel.icon

        val newTrackLabel = JBLabel()
        newTrackLabel.text = state.title

        val trackLabel = toolWindowContent.components[componentIndex++] as JBLabel
        trackLabel.text = newTrackLabel.text

        val newArtistAndAlbumLabel = JBLabel()
        newArtistAndAlbumLabel.text = "${state.artist} - ${state.album}"

        val artistAndAlbumLabel = toolWindowContent.components[componentIndex++] as JBLabel
        artistAndAlbumLabel.text = newArtistAndAlbumLabel.text

        val playtimeLabel = toolWindowContent.components[componentIndex] as JBLabel
        playtimeLabel.text = getPlaytimeText(state.seek, state.duration)
    }

    fun setContent() = JBPanel<JBPanel<*>>().apply {
        val state = service.fetchState()

        val imageUri = URI("${volumioHost}${state.albumart}")
        val icon = ImageIcon(imageUri.toURL())
        val albumartLabel = JBLabel(scaleImage(icon, 250, 250))

        layout = GridBagLayout()
        val c = GridBagConstraints()

        c.gridx = 0
        c.gridy = 0
        c.gridwidth = GridBagConstraints.REMAINDER
        add(albumartLabel, c)

        val trackLabel = JBLabel()
        trackLabel.text = state.title
        trackLabel.font = trackLabel.font.deriveFont(20.0f)
        trackLabel.border = BorderFactory.createEmptyBorder(10, 0, 0, 0)

        c.gridy++
        add(trackLabel, c)

        val artistAndAlbumLabel = JBLabel()
        artistAndAlbumLabel.text = "${state.artist} - ${state.album}"
        artistAndAlbumLabel.font = artistAndAlbumLabel.font.deriveFont(12.0f)
        artistAndAlbumLabel.border = BorderFactory.createEmptyBorder(5, 0, 0, 0)

        c.gridy++
        add(artistAndAlbumLabel, c)

        val playtimeLabel = JBLabel()
        playtimeLabel.text = getPlaytimeText(state.seek, state.duration)
        playtimeLabel.font = playtimeLabel.font.deriveFont(12.0f)
        playtimeLabel.border = BorderFactory.createEmptyBorder(5, 0, 0, 0)

        c.gridy++
        add(playtimeLabel, c)
    }

    private fun getPlaytimeText(seek: Long, duration: Long): String {
        // seekはミリ秒なので、秒に変換する
        val seekSec = seek / 1000
        val durationMinutes = duration / 60
        val durationSeconds = duration % 60
        val seekMinutes = seekSec / 60
        val seekSeconds = seekSec % 60

        return String.format("%02d:%02d / %02d:%02d", seekMinutes, seekSeconds, durationMinutes, durationSeconds)
    }

    private fun scaleImage(icon: ImageIcon, w: Int, h: Int): ImageIcon {
        var nw: Int = icon.iconWidth
        var nh: Int = icon.iconHeight

        if (icon.iconWidth > w) {
            nw = w
            nh = (nw * icon.iconHeight) / icon.iconWidth
        }

        if (nh > h) {
            nh = h
            nw = (icon.iconWidth * nh) / icon.iconHeight
        }

        return ImageIcon(icon.image.getScaledInstance(nw, nh, Image.SCALE_DEFAULT))
    }
}