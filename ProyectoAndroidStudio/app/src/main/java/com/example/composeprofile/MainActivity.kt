package com.example.composeprofile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Actividad Principal:
 * Punto de entrada habitual en una app Android. Aquí conectamos
 * Jetpack Compose mediante setContent { }.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ProfileScreen()
                }
            }
        }
    }
}

/**
 * 1. Composable "Stateful" (Con Estado):
 * Se encarga de guardar y gestionar el estado reactivo de la pantalla.
 * Aplica el patrón "State Hoisting" (elevación de estado) al pasar los datos
 * y las lambdas de eventos hacia la tarjeta visual.
 */
@Composable
fun ProfileScreen() {
    // rememberSaveable permite que estos valores sobrevivan incluso si giramos la pantalla
    var isFollowing by rememberSaveable { mutableStateOf(false) }
    var followersCount by rememberSaveable { mutableIntStateOf(342) }
    var likesCount by rememberSaveable { mutableIntStateOf(120) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        ProfileCard(
            name = "Edwin Leiva",
            role = "Estudiante de Ingeniería de Software",
            bio = "Explorando interfaces declarativas modernas con Jetpack Compose y Kotlin.",
            isFollowing = isFollowing,
            followers = followersCount,
            likes = likesCount,
            onFollowClick = {
                // Al cambiar isFollowing, Compose recompondrá solo los elementos afectados
                isFollowing = !isFollowing
                followersCount += if (isFollowing) 1 else -1
            },
            onLikeClick = {
                likesCount++
            }
        )
    }
}

/**
 * 2. Composable "Stateless" (Sin Estado Propio / Reutilizable):
 * Solo sabe dibujar la tarjeta según los parámetros que recibe.
 * Si el usuario interactúa, dispara los callbacks hacia arriba.
 * Esto hace que sea 100% testeable y reutilizable en cualquier pantalla.
 */
@Composable
fun ProfileCard(
    name: String,
    role: String,
    bio: String,
    isFollowing: Boolean,
    followers: Int,
    likes: Int,
    onFollowClick: () -> Unit,
    onLikeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth(0.92f),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar con iniciales
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = name.take(2).uppercase(),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Información de texto
            Text(
                text = name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = role,
                fontSize = 13.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = bio,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Fila de estadísticas (reutilizando el composable atómico StatItem)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(label = "Seguidores", count = followers)
                StatItem(label = "Me gusta", count = likes)
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Botones interactivos
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Botón dinámico: cambia de color y texto reactivamente
                Button(
                    onClick = onFollowClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isFollowing) Color(0xFF2E7D32) else MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(text = if (isFollowing) "Siguiendo ✓" else "Seguir")
                }

                OutlinedButton(
                    onClick = onLikeClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Dar Like ❤️")
                }
            }
        }
    }
}

/**
 * 3. Composable atómico reutilizable:
 * Muestra cualquier métrica con su contador y etiqueta.
 */
@Composable
fun StatItem(label: String, count: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = count.toString(),
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = Color.Gray
        )
    }
}

/**
 * 4. Previsualización en Android Studio:
 * Permite renderizar y validar la interfaz en el panel de diseño sin compilar en un dispositivo.
 */
@Preview(showBackground = true)
@Composable
fun PreviewProfileScreen() {
    MaterialTheme {
        ProfileScreen()
    }
}
