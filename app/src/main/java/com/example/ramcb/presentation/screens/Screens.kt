package com.example.ramcb.presentation.screens

import com.example.ramcb.presentation.screens.characterdetails.CharacterDetailsScreen

enum class Screens(
    private val args: List<String>? = null,
) {
    Dashboard,
    CharacterDetails(listOf(CharacterDetailsScreen.CHARACTER_ID_BUNDLE_KEY));

    operator fun invoke(): String {
        val argList = StringBuilder()
        args?.let { nnArgs ->
            nnArgs.forEach { arg -> argList.append("/{$arg}") }
        }
        return name + argList
    }

    fun withArgs(vararg args: Any): String {
        val destination = StringBuilder()
        args.forEach { arg -> destination.append("/$arg") }
        return name + destination
    }

}