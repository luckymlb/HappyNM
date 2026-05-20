package com.happynm.widget

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.happynm.widget.data.datastore.SettingsDataStore
import com.happynm.widget.data.model.UserSettings
import com.happynm.widget.domain.SalaryCalculator
import com.happynm.widget.widget.MainWidget
import com.happynm.widget.widget.SalaryWidget
import androidx.glance.appwidget.updateAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HappyNMTheme {
                SettingsScreen()
            }
        }
    }
}

@Composable
private fun HappyNMTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF2ECC71),
            secondary = Color(0xFF4A90D9),
            surface = Color.White,
            background = Color(0xFFFAFBFC),
            onSurface = Color(0xFF1A1A2E),
            onBackground = Color(0xFF1A1A2E)
        ),
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = remember { SettingsDataStore(context) }

    var settings by remember { mutableStateOf(UserSettings()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        settings = dataStore.settingsFlow.first()
        isLoading = false
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize().background(Color(0xFFFAFBFC)), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color(0xFF2ECC71))
        }
        return
    }

    val status = SalaryCalculator.calculate(settings)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("工作日薪", color = Color(0xFF1A1A2E)) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFFAFBFC))
            )
        },
        containerColor = Color(0xFFFAFBFC)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // 实时预览卡片
            EarningsPreviewCard(status)

            Spacer(modifier = Modifier.height(24.dp))

            // 薪资设置
            SettingsSection(title = "💰 薪资设置") {
                SettingsTextField(
                    label = "月薪 (元)",
                    value = if (settings.monthlySalary > 0) settings.monthlySalary.toLong().toString() else "",
                    onValueChange = { value ->
                        settings = settings.copy(monthlySalary = value.toDoubleOrNull() ?: 0.0)
                    },
                    keyboardType = KeyboardType.Number
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 工作时间设置
            SettingsSection(title = "⏰ 工作时间") {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("上班时间", color = Color(0xFF8E8E93), fontSize = 12.sp)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            TimeInput(
                                hour = settings.workStartHour,
                                minute = settings.workStartMinute,
                                onChanged = { h, m ->
                                    settings = settings.copy(workStartHour = h, workStartMinute = m)
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("下班时间", color = Color(0xFF8E8E93), fontSize = 12.sp)
                        TimeInput(
                            hour = settings.workEndHour,
                            minute = settings.workEndMinute,
                            onChanged = { h, m ->
                                settings = settings.copy(workEndHour = h, workEndMinute = m)
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                SettingsTextField(
                    label = "午休时长 (分钟)",
                    value = settings.lunchBreakMinutes.toString(),
                    onValueChange = { value ->
                        settings = settings.copy(lunchBreakMinutes = value.toIntOrNull() ?: 60)
                    },
                    keyboardType = KeyboardType.Number
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 倒计时设置
            SettingsSection(title = "📅 倒计时") {
                SettingsTextField(
                    label = "目标日期 (格式: 2026-06-15)",
                    value = settings.targetDateStr,
                    onValueChange = { value ->
                        settings = settings.copy(targetDateStr = value)
                    },
                    keyboardType = KeyboardType.Text
                )
                Spacer(modifier = Modifier.height(8.dp))
                SettingsTextField(
                    label = "标签",
                    value = settings.targetLabel,
                    onValueChange = { value ->
                        settings = settings.copy(targetLabel = value)
                    },
                    keyboardType = KeyboardType.Text
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 保存按钮
            Button(
                onClick = {
                    scope.launch {
                        dataStore.saveSettings(settings)
                        MainWidget().updateAll(context)
                        SalaryWidget().updateAll(context)
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2ECC71)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("保存并更新小组件", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun EarningsPreviewCard(status: SalaryCalculator.SalaryStatus) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEDF8F0)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("今日已赚", color = Color(0xFF1B9E5A), fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            if (status.isWorkDay) {
                Text(
                    text = "¥${"%.2f".format(status.earned)}",
                    color = Color(0xFF1B9E5A),
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { status.progress },
                    modifier = Modifier.fillMaxWidth().height(6.dp),
                    color = Color(0xFF4A90D9),
                    trackColor = Color(0xFFE5E7EB)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(status.statusText, color = Color(0xFF6B7280), fontSize = 12.sp)
                    Text("${(status.progress * 100).toInt()}%", color = Color(0xFF4A90D9), fontSize = 12.sp)
                }
            } else {
                Text("今日休息 ☀️", color = Color(0xFF6B7280), fontSize = 24.sp)
            }
        }
    }
}

@Composable
private fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, color = Color(0xFF1A1A2E), fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
private fun SettingsTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color(0xFF1A1A2E),
            unfocusedTextColor = Color(0xFF1A1A2E),
            focusedBorderColor = Color(0xFF2ECC71),
            unfocusedBorderColor = Color(0xFFE5E7EB),
            focusedLabelColor = Color(0xFF2ECC71),
            unfocusedLabelColor = Color(0xFF9CA3AF),
            cursorColor = Color(0xFF2ECC71)
        ),
        singleLine = true
    )
}

@Composable
private fun TimeInput(hour: Int, minute: Int, onChanged: (Int, Int) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = hour.toString().padStart(2, '0'),
            onValueChange = { v ->
                val h = v.filter { it.isDigit() }.take(2).toIntOrNull()?.coerceIn(0, 23) ?: hour
                onChanged(h, minute)
            },
            modifier = Modifier.width(60.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color(0xFF1A1A2E),
                unfocusedTextColor = Color(0xFF1A1A2E),
                focusedBorderColor = Color(0xFF2ECC71),
                unfocusedBorderColor = Color(0xFFE5E7EB),
                cursorColor = Color(0xFF2ECC71)
            ),
            singleLine = true
        )
        Text(" : ", color = Color(0xFF1A1A2E), fontSize = 18.sp)
        OutlinedTextField(
            value = minute.toString().padStart(2, '0'),
            onValueChange = { v ->
                val m = v.filter { it.isDigit() }.take(2).toIntOrNull()?.coerceIn(0, 59) ?: minute
                onChanged(hour, m)
            },
            modifier = Modifier.width(60.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color(0xFF1A1A2E),
                unfocusedTextColor = Color(0xFF1A1A2E),
                focusedBorderColor = Color(0xFF2ECC71),
                unfocusedBorderColor = Color(0xFFE5E7EB),
                cursorColor = Color(0xFF2ECC71)
            ),
            singleLine = true
        )
    }
}
