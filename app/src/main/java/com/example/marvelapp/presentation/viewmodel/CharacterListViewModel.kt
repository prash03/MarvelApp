
package com.example.marvelapp.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvelapp.data.repository.CharacterRepository
import com.example.marvelapp.domain.model.Character
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CharacterListViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = CharacterRepository(application.applicationContext)

    private val _characters = MutableStateFlow<List<Character>>(emptyList())
    val characters: StateFlow<List<Character>> = _characters

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var page = 0
    private var isLastPage = false

    init {
        loadMore()
    }

    fun loadMore() {
        if (isLastPage || _isLoading.value) return

        _isLoading.value = true
        viewModelScope.launch {
            try {
                // Adding a small delay to simulate network call and show the loader
                delay(1000)
                val newItems = repo.getCharacters(page)
                if (newItems.isEmpty()) {
                    isLastPage = true
                } else {
                    _characters.value = _characters.value + newItems
                    page++
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
