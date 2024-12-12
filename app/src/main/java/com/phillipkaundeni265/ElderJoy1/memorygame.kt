package com.phillipkaundeni265.ElderJoy1

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity1 : AppCompatActivity() {
    private lateinit var gridLayout: GridLayout
    private lateinit var scoreText: TextView
    private lateinit var timerText: TextView
    private lateinit var newGameButton: Button

    private val buttons = mutableListOf<Button>()
    private var firstCard: Button? = null
    private var secondCard: Button? = null
    private var score = 0
    private var matchedPairs = 0
    private var isProcessing = false

    private val images = listOf(
        "🌸", "🌸",  // Flowers
        "🌳", "🌳",  // Trees
        "🐈", "🐈",  // Cats
        "🐕", "🐕",  // Dogs
        "🍎", "🍎",  // Apples
        "🏠", "🏠",  // Houses
        "🌞", "🌞",  // Sun
        "⭐", "⭐"   // Star
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main1)

        gridLayout = findViewById(R.id.gridLayout)
        scoreText = findViewById(R.id.scoreText)
        timerText = findViewById(R.id.timerText)
        newGameButton = findViewById(R.id.newGameButton)

        setupGame()

        newGameButton.setOnClickListener {
            resetGame()
        }
    }

    private fun setupGame() {
        val shuffledImages = images.shuffled()

        for (i in shuffledImages.indices) {
            val button = Button(this).apply {
                layoutParams = GridLayout.LayoutParams().apply {
                    width = 0
                    height = 0
                    columnSpec = GridLayout.spec(i % 4, 1f)
                    rowSpec = GridLayout.spec(i / 4, 1f)
                    setMargins(8, 8, 8, 8)
                }
                textSize = 24f
                background = ContextCompat.getDrawable(context, R.drawable.card_background)
                tag = shuffledImages[i]
                setOnClickListener { onCardClick(this) }
            }
            buttons.add(button)
            gridLayout.addView(button)
        }
    }

    private fun onCardClick(button: Button) {
        if (isProcessing || button == firstCard || button.text.isNotEmpty()) return

        button.text = button.tag as String

        when {
            firstCard == null -> {
                firstCard = button
            }
            secondCard == null -> {
                secondCard = button
                checkMatch()
            }
        }
    }

    private fun checkMatch() {
        isProcessing = true

        if (firstCard?.tag == secondCard?.tag) {
            score += 10
            matchedPairs++

            firstCard?.isEnabled = false
            secondCard?.isEnabled = false

            if (matchedPairs == 8) {
                scoreText.text = "Game Complete! Score: $score"
            } else {
                scoreText.text = "Score: $score"
            }

            resetCards()
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                firstCard?.text = ""
                secondCard?.text = ""
                resetCards()
            }, 1000)
        }
    }

    private fun resetCards() {
        firstCard = null
        secondCard = null
        isProcessing = false
    }

    private fun resetGame() {
        score = 0
        matchedPairs = 0
        scoreText.text = "Score: 0"

        buttons.forEach { button ->
            button.text = ""
            button.isEnabled = true
        }

        val shuffledImages = images.shuffled()
        for (i in buttons.indices) {
            buttons[i].tag = shuffledImages[i]
        }
    }
}
