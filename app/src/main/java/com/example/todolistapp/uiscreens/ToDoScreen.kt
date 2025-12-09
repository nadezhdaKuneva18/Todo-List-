package com.example.todolistapp.uiscreens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.todolistapp.domain.ToDo
import com.example.todolistapp.presentation.TodoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToDoScreen(
    todoViewModel: TodoViewModel,
    onToggleTheme: () -> Unit
) {

    var task by remember { mutableStateOf("") }
    val todoList by todoViewModel.todos.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ToDo List") },

                // 🔥 Добавяме бутона за смяна на тема
                actions = {
                    IconButton(onClick = onToggleTheme) {
                        Icon(
                            imageVector = Icons.Filled.DarkMode,
                            contentDescription = "Toggle theme",
                            tint = Color.White
                        )
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Red,
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {

            // Основна колона с padding в долната част за бутона
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 80.dp)
            ) {

                // Add new task row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = task,
                        onValueChange = { task = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Enter task") },
                        shape = RoundedCornerShape(22.dp)
                    )

                    Button(
                        onClick = {
                            if (task.isNotBlank()) {
                                todoViewModel.addToDo(task)
                                task = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(Color.Red)
                    ) {
                        Text("Add", color = Color.White)
                    }
                }

                Spacer(Modifier.height(16.dp))
                Divider(color = Color.Red, thickness = 2.dp)
                Spacer(Modifier.height(12.dp))

                // Списък със задачи
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(todoList, key = { it.id }) { todo ->
                        ToDoItem(todo, todoViewModel, context)
                    }
                }
            }

            // Share button винаги в долната част
            Button(
                onClick = {
                    if (todoList.isNotEmpty()) {
                        val textToShare = todoList.joinToString("\n") { todo -> todo.title }
                        shareText(context, textToShare)
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(Color.Red)
            ) {
                Text("Share Todos", color = Color.White)
            }
        }
    }
}

@Composable
fun ToDoItem(todo: ToDo, todoViewModel: TodoViewModel, context: Context) {
    var isEditing by remember(todo.id) { mutableStateOf(false) }
    var newTitle by remember(todo.id) { mutableStateOf(todo.title) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = todo.isDone,
                onCheckedChange = { todoViewModel.togglrToDoDone(todo) }
            )

            if (isEditing) {
                OutlinedTextField(
                    value = newTitle,
                    onValueChange = { newTitle = it },
                    modifier = Modifier.width(160.dp)
                )

                Button(
                    onClick = {
                        if (newTitle.isNotBlank()) {
                            todoViewModel.editToDo(todo, newTitle)
                            isEditing = false
                        }
                    },
                    modifier = Modifier.padding(start = 4.dp)
                ) {
                    Text("Save")
                }

            } else {
                Text(
                    text = todo.title,
                    modifier = Modifier.padding(start = 8.dp),
                    style = if (todo.isDone)
                        LocalTextStyle.current.copy(
                            textDecoration = TextDecoration.LineThrough
                        )
                    else LocalTextStyle.current
                )
            }
        }

        Row {
            IconButton(onClick = {
                if (!isEditing) newTitle = todo.title
                isEditing = !isEditing
            }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit")
            }

            IconButton(onClick = {
                todoViewModel.deleteToDo(todo)
            }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }

            IconButton(onClick = {
                shareText(context, todo.title)
            }) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                    tint = Color.Red
                )
            }
        }
    }
}

fun shareText(context: Context, text: String) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, "Share To-Do List")
    context.startActivity(shareIntent)
}
