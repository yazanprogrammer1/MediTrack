package com.example.meditrack.feature.presentation.common

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.core.os.LocaleListCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.navigation.NavHostController
import com.example.meditrack.R
import com.example.meditrack.feature.presentation.navgraph.Routs
import com.example.meditrack.ui.theme.AppThemeSettings
import com.example.meditrack.ui.theme.DarkPrimaryColor
import com.example.meditrack.ui.theme.dimens
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

@Composable
fun AppDrawer(
    dataStore: DataStore<Preferences>,
    coroutineScope: CoroutineScope,
    navController: NavHostController,
    drawerState: DrawerState,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        DrawerHeader()
        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant,
            thickness = 1.dp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        DrawerContent(
            coroutineScope = coroutineScope,
            dataStore = dataStore,
            navController = navController,
            drawerState = drawerState
        )
        Spacer(modifier = Modifier.weight(1f))
        DrawerFooter(
            modifier = Modifier,
            dataStore = dataStore,
            coroutineScope = coroutineScope
        )
    }
}

@Preview
@Composable
private fun DrawerHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(
                Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.primary,
                        Color.White
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Card(
                shape = CircleShape,
                colors = CardDefaults.cardColors(containerColor = DarkPrimaryColor),
                elevation = CardDefaults.cardElevation(6.dp),
                modifier = Modifier.size(80.dp),
                onClick = {}
            ) {
                Image(
                    painter = painterResource(
                        id = if (AppThemeSettings.isDarkTheme) R.drawable.logo else R.drawable.logo
                    ),
                    contentDescription = null,
                    modifier = Modifier.padding(5.dp)
                )
            }
            Spacer(Modifier.height(12.dp))
            Text(
                text = appNameStyle(),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = "Your health, simplified",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.Black
                )
            )
        }
    }
}

@Composable
private fun DrawerContent(
    modifier: Modifier = Modifier,
    dataStore: DataStore<Preferences>,
    navController: NavHostController,
    drawerState: DrawerState,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
) {
    val context = LocalContext.current

    Column(
        modifier = modifier.padding(horizontal = 12.dp)
    ) {
        DrawerItem(
            icon = Icons.Default.ContentPaste,
            label = stringResource(id = R.string.instructions_before_use)
        ) {
            coroutineScope.launch { drawerState.close() }
            navController.navigate(Routs.Instructions.rout) {
                launchSingleTop = true
            }
        }

        DrawerItem(
            icon = Icons.Default.Share,
            label = stringResource(id = R.string.share_app)
        ) {
            coroutineScope.launch {
                drawerState.close()
                shareAppAsAPK(context)
            }
        }

        DrawerItem(
            icon = Icons.Default.Link,
            label = stringResource(id = R.string.share_download_link)
        ) {
            coroutineScope.launch { drawerState.close() }
            shareAppAsLink(context)
        }

        Spacer(modifier = Modifier.height(20.dp))

        LocaleDropdownMenu(scope = coroutineScope, dataStore = dataStore)
    }
}

@Composable
private fun DrawerItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            shape = CircleShape,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.padding(8.dp)
            )
        }
        Spacer(Modifier.width(16.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
        )
    }
}

@Composable
private fun DrawerFooter(
    modifier: Modifier = Modifier,
    dataStore: DataStore<Preferences>,
    coroutineScope: CoroutineScope,
    onProfileClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
) {
    val isDarkTheme = AppThemeSettings.isDarkTheme
    var isFingerprintEnabled by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // 🔹 Account Actions
        DrawerFooterItem(
            icon = Icons.Default.AccountCircle,
            label = stringResource(id = R.string.profile),
            onClick = onProfileClick
        )

        DrawerFooterItem(
            icon = Icons.Default.Logout,
            label = stringResource(id = R.string.logout),
            onClick = onLogoutClick
        )

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

        Spacer(modifier = Modifier.height(12.dp))

        FooterSwitchRow(
            title = stringResource(id = R.string.theme),
            checked = isDarkTheme,
            onCheckedChange = {
                coroutineScope.launch {
                    dataStore.edit { prefs ->
                        val key = booleanPreferencesKey("isDarkTheme")
                        prefs[key] = !AppThemeSettings.isDarkTheme
                    }
                }
            },
            icon = if (isDarkTheme) Icons.Default.DarkMode else Icons.Default.LightMode
        )

//        FooterSwitchRow(
//            title = stringResource(id = R.string.fingerprint),
//            checked = isFingerprintEnabled,
//            onCheckedChange = { isFingerprintEnabled = it },
//            icon = Icons.Default.Fingerprint
//        )
    }
}

@Composable
private fun DrawerFooterItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(vertical = 10.dp, horizontal = 8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
        )
    }
}

@Composable
private fun FooterSwitchRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    icon: ImageVector,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            thumbContent = {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocaleDropdownMenu(
    scope: CoroutineScope,
    dataStore: DataStore<Preferences>,
) {

    val context = LocalContext.current

    var selectedLang by rememberSaveable {
        mutableStateOf(context.getString(R.string.system))
    }

    LaunchedEffect(key1 = Unit) {
        dataStore.edit {
            val lang = it[stringPreferencesKey("appLang")] ?: context.getString(R.string.system)
            if (lang == "system") {
                selectedLang = context.getString(R.string.system)
            } else {
                selectedLang = lang
            }
        }
    }

    val localeOptions = mapOf(
        R.string.en to "en",
        R.string.ar to "ar",

        ).mapKeys { stringResource(it.key) }

    var expanded by remember {
        mutableStateOf(false)
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        TextField(
            readOnly = true,
            value = selectedLang,
            onValueChange = { },
            trailingIcon = {
                ExposedDropdownMenuDefaults.CustomTrailingIcon(
                    expanded = expanded,
                    size = MaterialTheme.dimens.appDrawerDimens.dropDownMenuExposedIconSize
                )
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {

            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(R.string.system),
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize
                    )
                },
                onClick = {
                    expanded = false
                    scope.launch(Dispatchers.Main) {
                        delay(200)
                        AppCompatDelegate.setApplicationLocales(
                            LocaleListCompat.getEmptyLocaleList()
                        )
                    }

                    scope.launch(Dispatchers.IO) {
                        dataStore.edit {
                            it[stringPreferencesKey("appLang")] = "system"
                        }
                    }
                }
            )

            localeOptions.keys.forEach { selectionLocale ->
                DropdownMenuItem(
                    onClick = {
                        expanded = false

                        scope.launch(Dispatchers.Main) {
                            delay(200)
                            // set app locale given the user's selected locale
                            AppCompatDelegate.setApplicationLocales(
                                LocaleListCompat.forLanguageTags(
                                    localeOptions[selectionLocale]
                                )
                            )
                        }

                        scope.launch(Dispatchers.IO) {
                            dataStore.edit {
                                it[stringPreferencesKey("appLang")] = selectionLocale
                            }
                        }


                    },
                    text = {
                        Text(
                            text = selectionLocale,
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize
                        )
                    }
                )
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun ExposedDropdownMenuDefaults.CustomTrailingIcon(
    expanded: Boolean,
    size: Dp,
) {
    Icon(
        Icons.Filled.ArrowDropDown,
        null,
        Modifier
            .size(size)
            .rotate(if (expanded) 180f else 0f),
    )
}


private suspend fun shareAppAsAPK(context: Context) {
    val app: ApplicationInfo = context.applicationInfo
    val originalApk = app.publicSourceDir
    val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Toast.makeText(context, throwable.message, Toast.LENGTH_SHORT).show()
    }

    withContext(Dispatchers.IO + coroutineExceptionHandler) {
        try {
            //Make new directory in new location
            var tempFile: File =
                File(context.getExternalFilesDir(null).toString() + "/ExtractedApk")
            //If directory doesn't exists create new
            if (!tempFile.isDirectory) if (!tempFile.mkdirs()) return@withContext
            //rename apk file to app name
            tempFile = File(
                tempFile.path + "/" + context.getString(app.labelRes).replace(" ", "") + ".apk"
            )
            //If file doesn't exists create new
            if (!tempFile.exists()) {
                if (!tempFile.createNewFile()) {
                    return@withContext
                }
            }
            //Copy file to new location
            val inp: InputStream = FileInputStream(originalApk)
            val out: OutputStream = FileOutputStream(tempFile)
            val buf = ByteArray(1024)
            var len: Int
            while (inp.read(buf).also { len = it } > 0) {
                out.write(buf, 0, len)
            }
            inp.close()
            out.close()
            withContext(Dispatchers.Main) {
                //Open share dialog
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "application/vnd.android.package-archive"
                val uri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider",
                    File(tempFile.path)
                )
                intent.putExtra(Intent.EXTRA_STREAM, uri)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                context.startActivity(intent)
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}


private fun shareAppAsLink(context: Context) {
    val url = "https://drive.google.com/file/d/1oG5JbIdrURFXQ8V45Ovloyik5k2LBIK6/view?usp=sharing"

    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        putExtra(Intent.EXTRA_TEXT, url)
        type = "text/plain" // 👍 بدل Intent.setType
    }

    val openIntent = Intent(Intent.ACTION_VIEW).apply {
        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION // 👍 بدل Intent.setFlags
        data = url.toUri() // 👍 بدل Intent.setData
    }

    val shareIntent = Intent.createChooser(
        sendIntent,
        ContextCompat.getContextForLanguage(context).getString(R.string.share_link_via)
    ).apply {
        putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(openIntent))
    }

    context.startActivity(shareIntent)
}






