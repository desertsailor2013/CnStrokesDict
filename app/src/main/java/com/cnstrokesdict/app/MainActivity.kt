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
import com.cnstrokesdict.app.ui.home.SearchScreen
import com.cnstrokesdict.app.ui.theme.CnStrokesTheme
import com.cnstrokesdict.app.vm.DictionaryViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CnStrokesTheme {
                // 避免 edge-to-edge 下窗口默认背景为黑/透明导致整屏发黑；并保证根布局占满屏幕
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
    }
}
