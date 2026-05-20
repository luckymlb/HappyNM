package com.happynm.widget.widget

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.glance.state.GlanceStateDefinition
import com.happynm.widget.data.datastore.settingsDataStore
import java.io.File

object SettingsGlanceStateDefinition : GlanceStateDefinition<Preferences> {

    override suspend fun getDataStore(context: Context, fileKey: String): DataStore<Preferences> {
        return context.settingsDataStore
    }

    override fun getLocation(context: Context, fileKey: String): File {
        return File(context.filesDir, "datastore/happynm_settings.preferences_pb")
    }
}
