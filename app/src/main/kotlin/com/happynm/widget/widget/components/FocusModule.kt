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
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.happynm.widget.MainActivity

@Composable
fun FocusModule(modifier: GlanceModifier = GlanceModifier) {
    Column(
        modifier = modifier
            .background(ColorProvider(Color(0xFF2A2040)))
            .padding(10.dp)
            .cornerRadius(12.dp)
            .clickable(actionStartActivity<MainActivity>())
    ) {
        Text(
            text = "🍅",
            style = TextStyle(fontSize = 16.sp)
        )
        Spacer(modifier = GlanceModifier.height(2.dp))
        Text(
            text = "专注时刻",
            style = TextStyle(
                color = ColorProvider(Color(0xFF8B5CF6)),
                fontSize = 13.sp
            )
        )
        Text(
            text = "点击开始",
            style = TextStyle(
                color = ColorProvider(Color(0xFF8E8E93)),
                fontSize = 11.sp
            )
        )
    }
}
