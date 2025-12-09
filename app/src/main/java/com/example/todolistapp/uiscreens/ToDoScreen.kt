package com.example.todolistapp.uiscreens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextDecoration
import com.example.todolistapp.presentation.TodoViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToDoScreen(todoViewModel: TodoViewModel) {

    var task by remember { mutableStateOf("") }
    val todoList by todoViewModel.todos.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "ToDo List") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Red,
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = task,
                    onValueChange = { task = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Enter task") },
                    shape = RoundedCornerShape(22.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Red,
                        unfocusedIndicatorColor = Color.Red,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
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
                    Text("Add")
                }
            }

            Spacer(Modifier.height(16.dp))

            HorizontalDivider(color = Color.Red)

            Spacer(Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(todoList, key = { it.id }) { todo ->

                    var isEditing by remember(todo.id) { mutableStateOf(false) }
                    var newTitle by remember(todo.id) { mutableStateOf(todo.title) }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Row(verticalAlignment = Alignment.CenterVertically) {

                            Checkbox(
                                checked = todo.isDone,
                                onCheckedChange = { todoViewModel.togglrToDoDone(todo) },
                                colors = CheckboxDefaults.colors(checkedColor = Color.Red)
                            )

                            if (isEditing) {
                                OutlinedTextField(
                                    value = newTitle,
                                    onValueChange = { newTitle = it },
                                    modifier = Modifier.width(160.dp),
                                    shape = RoundedCornerShape(22.dp),
                                    colors = TextFieldDefaults.colors(
                                        focusedIndicatorColor = Color.Red,
                                        unfocusedIndicatorColor = Color.Red,
                                        focusedContainerColor = Color.White,
                                        unfocusedContainerColor = Color.White
                                    )
                                )

                                Button(
                                    onClick = {
                                        todoViewModel.editToDo(todo, newTitle)
                                        isEditing = false
                                    },
                                    modifier = Modifier.padding(start = 4.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
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
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit",
                                    tint = Color.Red)
                            }
                            IconButton(onClick = {
                                todoViewModel.deleteToDo(todo)
                            }) {
                                Icon(imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = Color.Red)
                            }

                        }
                    }
                }
            }
        }
    }
}
