package abh.arabic.search

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import abh.arabic.search.ui.theme.ArabicSearchTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ArabicSearchTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ArabicSearchScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun ArabicSearchScreen(modifier: Modifier = Modifier) {
    var contentText by remember { mutableStateOf("وَ اَشْهَدُ اَنْ لا اِلهَ اِلاَّ اللَّهُ") }
    var searchText by remember { mutableStateOf("اشهد") }
    var searchResult by remember { mutableStateOf<SearchResult?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Arabic Text Search",
            style = MaterialTheme.typography.headlineMedium
        )

        // Content Text Field
        OutlinedTextField(
            value = contentText,
            onValueChange = { contentText = it },
            label = { Text("Arabic Content") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            maxLines = 5
        )

        // Search Text Field
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Search Text") },
            modifier = Modifier.fillMaxWidth()
        )

        // Search Button
        Button(
            onClick = {
                searchResult = performSearch(contentText, searchText)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search")
        }

        // Results Section
        searchResult?.let { result ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Search Results:",
                        style = MaterialTheme.typography.titleMedium
                    )

                    // Show if found
                    Text(
                        text = "Found: ${if (result.isContain) "Yes ✓" else "No ✗"}",
                        color = if (result.isContain) Color(0xFF4CAF50) else Color(0xFFF44336),
                        style = MaterialTheme.typography.bodyLarge
                    )

                    if (result.isContain) {
                        // Show positions
                        Text(
                            text = "Start Position: ${result.startPosition}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "End Position: ${result.endPosition}",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Divider(modifier = Modifier.padding(vertical = 8.dp))

                        // Show highlighted text
                        Text(
                            text = "Highlighted Result:",
                            style = MaterialTheme.typography.titleSmall
                        )

                        Text(
                            text = result.highlightedText,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

data class SearchResult(
    val isContain: Boolean,
    val startPosition: Int,
    val endPosition: Int,
    val highlightedText: androidx.compose.ui.text.AnnotatedString
)

fun performSearch(contentText: String, searchText: String): SearchResult {
    val objSearch = ABHArabicDiacritics(contentText, searchText)

    // Get the SpannableString from Java class
    val spannableString = objSearch.searchHighlightedSpan
    val isContain = objSearch.isContain
    val startPosition = objSearch.searchTextStartPosition
    val endPosition = objSearch.searchTextEndPosition

    // Convert SpannableString to AnnotatedString for Compose
    val annotatedString = buildAnnotatedString {
        append(contentText)

        if (isContain && startPosition >= 0 && endPosition > startPosition) {
            val actualStart = if (startPosition > 0) startPosition - 1 else startPosition
            addStyle(
                style = SpanStyle(color = Color.Red),
                start = actualStart,
                end = endPosition
            )
        }
    }

    return SearchResult(
        isContain = isContain,
        startPosition = startPosition,
        endPosition = endPosition,
        highlightedText = annotatedString
    )
}