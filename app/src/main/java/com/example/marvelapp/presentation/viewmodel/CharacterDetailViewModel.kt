
package com.example.marvelapp.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvelapp.data.repository.CharacterRepository
import com.example.marvelapp.domain.model.Character
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CharacterDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = CharacterRepository(application.applicationContext)

    private val _character = MutableStateFlow<Character?>(null)
    val character: StateFlow<Character?> = _character

    fun getCharacter(id: Int) {
        viewModelScope.launch {
            val result = repo.getCharacterById(id)
            _character.value = result
        }
    }
}
