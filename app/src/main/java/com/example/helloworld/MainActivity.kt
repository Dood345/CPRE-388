package com.example.helloworld

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.random.Random // Added for random number generation

class MainActivity : AppCompatActivity() {

    private var clickCount = 0
    private lateinit var words: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Load the words from resources once
        words = resources.getStringArray(R.array.sentence_words)

        val button: Button = findViewById(R.id.button)
        val messageTextView: TextView = findViewById(R.id.textView) // Moved to onCreate for efficiency

        button.setOnClickListener {
            clickCount++
            if (clickCount == 1) {
                messageTextView.text = getString(R.string.secret)
            } else {
                // Generate a random sentence with 3 to 5 words
                val sentenceLength = Random.nextInt(3, 6) // Randomly 3, 4, or 5 words
                val randomSentence = (1..sentenceLength).joinToString(" ") { words.random() } // Simplified call chain
                val capitalizedSentence = randomSentence.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                messageTextView.text = getString(R.string.random_sentence_format, capitalizedSentence)
            }
            // finish() // Remains commented out
        }
    }
}