package com.happynm.widget.widget.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.background
import androidx.glance.appwidget.cornerRadius
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.happynm.widget.MainActivity

@Composable
fun FocusModule(modifier: GlanceModifier = GlanceModifier) {
    Column(
        modifier = modifier
            .background(ColorProvider(Color(0xFFF5F0FF)))
            .padding(10.dp)
            .cornerRadius(14.dp)
            .clickable(actionStartActivity<MainActivity>()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "🎯",
            style = TextStyle(fontSize = 16.sp)
        )
        Spacer(modifier = GlanceModifier.height(2.dp))
        Text(
            text = "专注",
            style = TextStyle(
                color = ColorProvider(Color(0xFF9B59B6)),
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
        )
    }
}
