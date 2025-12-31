# ArabicSearch 🔍



**Without nunation(harakath) contains in an Arabic String**

A powerful and lightweight Android library for searching Arabic text without diacritical marks (harakath/tashkeel). Perfect for Quran apps, Islamic text applications, Arabic dictionaries, and any app dealing with Arabic text search.

## ✨ Features

- 🎯 **Diacritic-Insensitive Search** - Search Arabic text ignoring all harakath marks
- 🎨 **Text Highlighting** - Automatically highlight matched text in red
- 📍 **Position Tracking** - Get exact start and end positions of matched text
- 🚀 **Fast & Efficient** - Optimized for performance
- 📱 **Android Compatible** - Works with both traditional Views and Jetpack Compose
- 🔧 **Easy Integration** - Simple API with just 2 classes

## 📦 Installation

### Download Java Files

Download the required Java classes:

- [ABHArabicDiacritics.java](https://arshadbinhamza.com/arabicsearch/ABHArabicDiacritics.java)
- [ArabicNormalizer.java](https://arshadbinhamza.com/arabicsearch/ArabicNormalizer.java)

Add them to your Android project in the appropriate package.

## 🚀 Quick Start

### 1️⃣ Simple Contains Check

Check if text exists without worrying about diacritics:

**Java:**
```java
ABHArabicDiacritics objSearch = new ABHArabicDiacritics();
String content = "وَ اَشْهَدُ اَنْ لا اِلهَ اِلاَّ اللَّهُ";
String searchTerm = "اشهد";

boolean found = objSearch.getDiacriticinsensitive(content).contains(searchTerm);
System.out.println("Found: " + found); // Output: Found: true
```

**Kotlin:**
```kotlin
val objSearch = ABHArabicDiacritics()
val content = "وَ اَشْهَدُ اَنْ لا اِلهَ اِلاَّ اللَّهُ"
val searchTerm = "اشهد"

val found = objSearch.getDiacriticinsensitive(content).contains(searchTerm)
println("Found: $found") // Output: Found: true
```

### 2️⃣ Highlighted Search Results

Display search results with highlighted matched text:

**Java:**
```java
String content = "وَ اَشْهَدُ اَنْ لا اِلهَ اِلاَّ اللَّهُ";
String searchTerm = "اشهد";

ABHArabicDiacritics objSearch = new ABHArabicDiacritics(content, searchTerm);

// Get highlighted SpannableString (matched text appears in red)
SpannableString highlighted = objSearch.getSearchHighlightedSpan();
textView.setText(highlighted);
```

**Kotlin:**
```kotlin
val content = "وَ اَشْهَدُ اَنْ لا اِلهَ اِلاَّ اللَّهُ"
val searchTerm = "اشهد"

val objSearch = ABHArabicDiacritics(content, searchTerm)

// Get highlighted SpannableString (matched text appears in red)
val highlighted = objSearch.searchHighlightedSpan
textView.text = highlighted
```

### 3️⃣ Get Search Positions

Get detailed information about the match:

**Java:**
```java
ABHArabicDiacritics objSearch = new ABHArabicDiacritics(content, searchTerm);

if (objSearch.isContain()) {
    int startPos = objSearch.getSearchTextStartPosition();
    int endPos = objSearch.getSearchTextEndPosition();
    
    System.out.println("Found at position: " + startPos + " to " + endPos);
}
```

**Kotlin:**
```kotlin
val objSearch = ABHArabicDiacritics(content, searchTerm)

if (objSearch.isContain) {
    val startPos = objSearch.searchTextStartPosition
    val endPos = objSearch.searchTextEndPosition
    
    println("Found at position: $startPos to $endPos")
}
```

## 📱 Complete Android Examples

### Traditional Android View (Kotlin)

```kotlin
class MainActivity : AppCompatActivity() {
    
    private lateinit var etContent: EditText
    private lateinit var etSearch: EditText
    private lateinit var tvResult: TextView
    private lateinit var btnSearch: Button
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        etContent = findViewById(R.id.etContent)
        etSearch = findViewById(R.id.etSearch)
        tvResult = findViewById(R.id.tvResult)
        btnSearch = findViewById(R.id.btnSearch)
        
        btnSearch.setOnClickListener {
            performSearch()
        }
    }
    
    private fun performSearch() {
        val content = etContent.text.toString()
        val searchTerm = etSearch.text.toString()
        
        val objSearch = ABHArabicDiacritics(content, searchTerm)
        
        if (objSearch.isContain) {
            tvResult.text = objSearch.searchHighlightedSpan
        } else {
            tvResult.text = "Not found"
        }
    }
}
```

### Jetpack Compose (Kotlin)

```kotlin
@Composable
fun ArabicSearchScreen() {
    var contentText by remember { mutableStateOf("وَ اَشْهَدُ اَنْ لا اِلهَ اِلاَّ اللَّهُ") }
    var searchText by remember { mutableStateOf("اشهد") }
    var searchResult by remember { mutableStateOf<SearchResult?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = contentText,
            onValueChange = { contentText = it },
            label = { Text("Arabic Content") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Search Text") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                searchResult = performSearch(contentText, searchText)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search")
        }

        searchResult?.let { result ->
            if (result.isContain) {
                Text(
                    text = result.highlightedText,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

fun performSearch(content: String, searchTerm: String): SearchResult {
    val objSearch = ABHArabicDiacritics(content, searchTerm)
    
    val annotatedString = buildAnnotatedString {
        append(content)
        if (objSearch.isContain) {
            val start = if (objSearch.searchTextStartPosition > 0) 
                objSearch.searchTextStartPosition - 1 
            else objSearch.searchTextStartPosition
            
            addStyle(
                style = SpanStyle(color = Color.Red),
                start = start,
                end = objSearch.searchTextEndPosition
            )
        }
    }
    
    return SearchResult(
        isContain = objSearch.isContain,
        startPosition = objSearch.searchTextStartPosition,
        endPosition = objSearch.searchTextEndPosition,
        highlightedText = annotatedString
    )
}
```

## 🔧 ArabicNormalizer Usage

The `ArabicNormalizer` class can be used standalone for text preprocessing:

**Java:**
```java
String arabicText = "كَلَّا لَا تُطِعْهُ وَاسْجُدْ وَاقْتَرِبْ ۩";
ArabicNormalizer normalizer = new ArabicNormalizer(arabicText);
String cleanText = normalizer.getOutput();

System.out.println("Before: " + arabicText);
System.out.println("After: " + cleanText);
// Output: كلا لا تطعه واسجد واقترب
```

**Kotlin:**
```kotlin
val arabicText = "كَلَّا لَا تُطِعْهُ وَاسْجُدْ وَاقْتَرِبْ ۩"
val normalizer = ArabicNormalizer(arabicText)
val cleanText = normalizer.output

println("Before: $arabicText")
println("After: $cleanText")
// Output: كلا لا تطعه واسجد واقترب
```

## 📖 What Gets Removed/Normalized

The library removes and normalizes the following:

### Removed:
- ✓ Honorific signs (ﷺ)
- ✓ Quranic annotations (۞, ۩, etc.)
- ✓ Tatweel (ـ)
- ✓ All tashkeel marks:
  - Fatha (َ), Damma (ُ), Kasra (ِ)
  - Shadda (ّ), Sukun (ْ)
  - Tanween marks (ً ٌ ٍ)
  - And many more...

### Normalized:
- أ إ آ → ا (All Alif forms → Alif)
- ي ئ → ى (Ya and Ya with Hamza → Alif Maksura)
- ة → ه (Ta Marbuta → Ha)
- ؤ → و (Waw with Hamza → Waw)

## 🎯 Use Cases

- 📖 **Quran Applications** - Search verses without typing diacritics
- 📚 **Islamic Text Apps** - Search hadith, duas, and Islamic content
- 📖 **Arabic Dictionaries** - Flexible word lookup
- 💬 **Chat/Messaging** - Search Arabic messages efficiently
- 📄 **Document Search** - Search through Arabic documents and articles

## 📊 API Reference

### ABHArabicDiacritics

#### Constructors
```java
ABHArabicDiacritics()                        // Default constructor
ABHArabicDiacritics(String content, String searchText)  // Search constructor
```

#### Methods
```java
boolean isContain()                          // Check if search text was found
SpannableString getSearchHighlightedSpan()   // Get highlighted result
int getSearchTextStartPosition()             // Get match start position
int getSearchTextEndPosition()               // Get match end position
String getDiacriticinsensitive(String s)     // Remove diacritics from string
```

### ArabicNormalizer

#### Constructor
```java
ArabicNormalizer(String input)               // Create normalizer with text
```

#### Methods
```java
String getOutput()                           // Get normalized text
```

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**Arshad Bin Hamza**

- Website: [arshadbinhamza.com](https://arshadbinhamza.com)


## 🙏 Acknowledgments

- Thanks to https://stackoverflow.com/users/3517232/ibrabel and https://gist.github.com/alierdogan7/ who contributed ArabicNormalizer Class
- Thanks to all contributors who have helped with this project  
- Inspired by the need for better Arabic text processing in Android

## 📚 Documentation

For complete documentation and examples, visit:
- [Complete Guide (HTML)](https://arshadbinhamza.com/arabicsearch/)
- [Java Documentation](https://arshadbinhamza.com/arabicsearch/ABHArabicDiacritics.java)

---

⭐ Star this repository if you find it helpful!

Made with ❤️ 
