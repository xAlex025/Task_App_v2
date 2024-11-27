package com.example.task_app_v2

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ListaDeTareasApp()
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ListaDeTareasApp() {
    var tareas by remember { mutableStateOf(listOf(
        Tarea(1, "Comprar comida", "Comprar frutas y verduras", PrioridadDeTarea.ALTA, false),
        Tarea(2, "Estudiar", "Estudiar para el examen de matemáticas", PrioridadDeTarea.MEDIA, false),
        Tarea(3, "Llamar al banco", "Solicitar información sobre mi cuenta", PrioridadDeTarea.BAJA, true),
        Tarea(4, "Hacer ejercicio", "30 minutos de ejercicio", PrioridadDeTarea.ALTA, true)
    )) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .systemBarsPadding()
    ) {
        Text(
            text = "Lista de Tareas",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(Alignment.CenterHorizontally)
        )
        ListaDeTareas(
            tareas = tareas,
            alActualizarTarea = { tareaActualizada ->
                tareas = tareas.map { if (it.id == tareaActualizada.id) tareaActualizada else it }
            }
        )
    }
}

@Composable
fun ListaDeTareas(tareas: List<Tarea>, alActualizarTarea: (Tarea) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Tareas Pendientes",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Divider(modifier = Modifier.padding(vertical = 8.dp))

        tareas.forEach { tarea ->
            if (!tarea.estaCompletada) {
                Tarjeta(tarea = tarea, alActualizarTarea = alActualizarTarea)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Divider(thickness = 1.dp)
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Tareas Completadas",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Divider(modifier = Modifier.padding(vertical = 8.dp), thickness = 1.dp)

        tareas.forEach { tarea ->
            if (tarea.estaCompletada) {
                Tarjeta(tarea = tarea, alActualizarTarea = alActualizarTarea)
            }
        }
    }
}

@Composable
fun Tarjeta(tarea: Tarea, alActualizarTarea: (Tarea) -> Unit) {
    var expandido by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFEFEFEF)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        tarea.titulo,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        tarea.descripcion,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                BadgedBox(
                    badge = {
                        Badge(
                            containerColor = when (tarea.prioridad) {
                                PrioridadDeTarea.ALTA -> Color.Red
                                PrioridadDeTarea.MEDIA -> Color.Yellow
                                PrioridadDeTarea.BAJA -> Color.Green
                            },
                            modifier = Modifier
                                .size(18.dp)
                                .clip(androidx.compose.foundation.shape.CircleShape)
                        )
                    }
                ) {
                    Box(modifier = Modifier.size(48.dp))
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Box {
                    TextButton(onClick = { expandido = true }) {
                        Text("Opciones", style = MaterialTheme.typography.bodyMedium)
                    }
                    DropdownMenu(
                        expanded = expandido,
                        onDismissRequest = { expandido = false },
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    if (tarea.estaCompletada) "Revertir a Pendiente" else "Marcar como Completada",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            },
                            onClick = {
                                alActualizarTarea(tarea.copy(estaCompletada = !tarea.estaCompletada))
                                expandido = false
                            }
                        )
                        PrioridadDeTarea.values().forEach { prioridad ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = when (prioridad) {
                                            PrioridadDeTarea.ALTA -> "Alta"
                                            PrioridadDeTarea.MEDIA -> "Media"
                                            PrioridadDeTarea.BAJA -> "Baja"
                                        },
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                },
                                onClick = {
                                    alActualizarTarea(tarea.copy(prioridad = prioridad))
                                    expandido = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

data class Tarea(
    val id: Int,
    val titulo: String,
    val descripcion: String,
    val prioridad: PrioridadDeTarea,
    val estaCompletada: Boolean
)

enum class PrioridadDeTarea { ALTA, MEDIA, BAJA }


