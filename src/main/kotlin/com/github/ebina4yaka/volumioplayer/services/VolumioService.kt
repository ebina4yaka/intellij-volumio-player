package com.github.ebina4yaka.volumioplayer.services

import com.github.ebina4yaka.volumioplayer.entity.VolumioState
import com.github.ebina4yaka.volumioplayer.repository.VolumioRestRepository
import com.intellij.openapi.components.Service
import kotlinx.coroutines.runBlocking

@Service(Service.Level.PROJECT)
class VolumioService(private val volumioHost: String = "http://volumio.local") {
    private val volumioRepo = VolumioRestRepository(volumioHost)

    fun fetchState(): VolumioState {
        return runBlocking {
            val state = volumioRepo.fetchState().body() ?: return@runBlocking VolumioState(
            )
            // albumartにドメインがない場合は、volumioHostを追加する
            if (!state.albumart.startsWith("http")) {
                return@runBlocking state.copy(albumart = "${volumioHost}${state.albumart}")
            }
            return@runBlocking state
        }
    }

    fun toggle() {
        volumioRepo.toggle()
    }

    fun next() {
        volumioRepo.next()
    }

    fun prev() {
        volumioRepo.prev()
    }
}
