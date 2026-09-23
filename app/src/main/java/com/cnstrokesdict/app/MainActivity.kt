package com.cnstrokesdict.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cnstrokesdict.app.ui.detail.DetailScreen
import com.cnstrokesdict.app.ui.dictation.DictationScreen
import com.cnstrokesdict.app.ui.home.SearchScreen
import com.cnstrokesdict.app.ui.qrscan.QrScanScreen
import com.cnstrokesdict.app.ui.qrshare.QrShareScreen
import com.cnstrokesdict.app.ui.theme.CnStrokesTheme
import com.cnstrokesdict.app.ui.update.UpdateScreen
import com.cnstrokesdict.app.ui.wordmanager.WordPackageManagerScreen
import com.cnstrokesdict.app.vm.DictionaryViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CnStrokesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    DictionaryApp()
                }
            }
        }
    }
}

private const val ROUTE_SEARCH = "search"
private const val ROUTE_DETAIL = "detail"
private const val ROUTE_WORD_MANAGER = "word_manager"
private const val ROUTE_QR_SHARE = "qr_share/{packageId}"
private const val ROUTE_QR_SCAN = "qr_scan"
private const val ROUTE_UPDATE = "update"
private const val ROUTE_DICTATION = "dictation"

@Composable
fun DictionaryApp() {
    val nav = rememberNavController()
    val scope = rememberCoroutineScope()
    val vm: DictionaryViewModel = viewModel(
        factory = DictionaryViewModel.factory(LocalContext.current.applicationContext),
    )

    NavHost(
        navController = nav,
        startDestination = ROUTE_SEARCH,
        modifier = Modifier.fillMaxSize(),
    ) {
        composable(ROUTE_SEARCH) {
            SearchScreen(
                state = vm.uiState,
                onQueryChange = vm::onQueryChange,
                onVoiceSearchResult = vm::onVoiceSearchResult,
                onPickChar = { item ->
                    scope.launch {
                        val entry = vm.loadEntryForDetail(item.character) ?: return@launch
                        vm.select(entry)
                        nav.navigate(ROUTE_DETAIL)
                    }
                },
                onOpenWordManager = { nav.navigate(ROUTE_WORD_MANAGER) },
                onOpenUpdate = { nav.navigate(ROUTE_UPDATE) },
                onOpenDictation = { nav.navigate(ROUTE_DICTATION) },
            )
        }
        composable(ROUTE_DETAIL) {
            val entry = vm.selected
            if (entry == null) {
                nav.popBackStack()
                return@composable
            }
            DetailScreen(
                entry = entry,
                onBack = { nav.popBackStack() },
            )
        }
        composable(ROUTE_WORD_MANAGER) {
            WordPackageManagerScreen(
                onBack = { nav.popBackStack() },
                onSharePackage = { packageId ->
                    nav.navigate("qr_share/$packageId")
                },
                onScanQrCode = { nav.navigate(ROUTE_QR_SCAN) },
            )
        }
        composable(ROUTE_QR_SHARE) { backStackEntry ->
            val packageId = backStackEntry.arguments?.getString("packageId")?.toIntOrNull()
            if (packageId == null) {
                nav.popBackStack()
                return@composable
            }
            QrShareScreen(
                packageId = packageId,
                onBack = { nav.popBackStack() },
            )
        }
        composable(ROUTE_QR_SCAN) {
            QrScanScreen(
                onBack = { nav.popBackStack() },
                onImportSuccess = { nav.popBackStack() },
            )
        }
        composable(ROUTE_UPDATE) {
            UpdateScreen(
                onBack = { nav.popBackStack() },
            )
        }
        composable(ROUTE_DICTATION) {
            DictationScreen(
                onBack = { nav.popBackStack() },
            )
        }
    }
}
