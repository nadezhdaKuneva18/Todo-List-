package com.example.todolistapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.todolistapp.presentation.ThemeViewModel
import com.example.todolistapp.presentation.TodoViewModel
import com.example.todolistapp.ui.theme.TodoListAppTheme
import com.example.todolistapp.uiscreens.ToDoScreen

class MainActivity : ComponentActivity() {

    // ViewModel за ToDo задачите
    private val todoViewModel: TodoViewModel by viewModels()

    // ViewModel за темата
    private val themeViewModel: ThemeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // правим приложението edge-to-edge

        setContent {
            // Взимаме текущото състояние на темата от ThemeViewModel
            val isDarkTheme = themeViewModel.isDarkTheme.value

            // Обвиваме цялото UI в нашата тема
            TodoListAppTheme(darkTheme = isDarkTheme) {
                Surface(color = MaterialTheme.colorScheme.background) {

                    // Извикваме ToDoScreen и предаваме:
                    // 1) todoViewModel – за списъка със задачи
                    // 2) onToggleTheme – функцията, която ще сменя темата при натискане на бутон
                    ToDoScreen(
                        todoViewModel = todoViewModel,
                        onToggleTheme = { themeViewModel.toggleTheme() }
                    )
                }
            }
        }
    }
}

// Глобална функция за споделяне на текст
fun ComponentActivity.shareText(text: String) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, null)
    startActivity(shareIntent)
}
