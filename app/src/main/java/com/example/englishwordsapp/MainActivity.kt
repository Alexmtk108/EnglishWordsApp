package com.example.englishwordsapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var englishWordTextView: TextView
    private lateinit var option1TextView: TextView
    private lateinit var option2TextView: TextView
    private lateinit var option3TextView: TextView
    private lateinit var option4TextView: TextView
    private lateinit var bottomConstrain: ConstraintLayout
    private lateinit var imageRight: ImageView
    private lateinit var textRight:TextView




    private var currentWord: Pair<String, String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn_word)

        val buttonNext = findViewById<Button>(R.id.buttonNext)
        buttonNext.setOnClickListener{
            nextWord()

        }

        englishWordTextView = findViewById<TextView>(R.id.englishWordTextView)
        option1TextView = findViewById(R.id.option1TextView)
        option2TextView = findViewById(R.id.option2TextView)
        option3TextView = findViewById(R.id.option3TextView)
        option4TextView = findViewById(R.id.option4TextView)
        bottomConstrain = findViewById(R.id.continueBottomConstrain)
        imageRight = findViewById(R.id.imageView)
        textRight = findViewById(R.id.textViewRight)

        lifecycleScope.launch { setNewWord() }

        val optionClickListener = View.OnClickListener { view ->
            val selectedOption = (view as TextView).text.toString()
            checkAnswer(selectedOption)
        }

        option1TextView.setOnClickListener(optionClickListener)
        option2TextView.setOnClickListener(optionClickListener)
        option3TextView.setOnClickListener(optionClickListener)
        option4TextView.setOnClickListener(optionClickListener)
    }

    private suspend fun setNewWord() {
        // Выбираем случайное слово
        currentWord = words.random()
        delay(1500)
        bottomConstrain.setBackgroundColor(ContextCompat.getColor(this, R.color.custom_grey))
        imageRight.visibility = View.INVISIBLE
        textRight.visibility = View.INVISIBLE
        // Обновляем текст главного TextView
        englishWordTextView.text = currentWord?.first
        //var right = findViewById<TextView>(R.id.textViewRight)
        val right = currentWord?.second
// Генерируем случайный порядок вариантов ответов
        val options = words.map { it.second }.shuffled()

// Создаем список вариантов ответов, добавляем правильный ответ и другие случайные ответы
        val randomAnswerList = mutableListOf<String?>()
        randomAnswerList.add(right) // Добавляем правильный ответ
        randomAnswerList.add(options[0])
        randomAnswerList.add(options[1])
        randomAnswerList.add(options[2])

// Перемешиваем список ответов
        randomAnswerList.shuffle()

// Устанавливаем варианты ответов в TextView
        option1TextView.text = randomAnswerList[0]
        option2TextView.text = randomAnswerList[1]
        option3TextView.text = randomAnswerList[2]
        option4TextView.text = randomAnswerList[3]
    }

    private fun checkAnswer(selectedOption: String) {
        if (selectedOption == currentWord?.second) {
            bottomConstrain.setBackgroundColor(ContextCompat.getColor(this, R.color.custom_green))
            //Toast.makeText(this, "Правильно!", Toast.LENGTH_SHORT).show()
            imageRight.setImageResource(R.drawable.ic_correct)
            imageRight.visibility = View.VISIBLE
            textRight.visibility = View.VISIBLE
            var textCorr = findViewById<TextView>(R.id.numCorrect)
            var num = textCorr.text.toString().toInt() + 1
            textCorr.text = num.toString()
        } else {
            bottomConstrain.setBackgroundColor(ContextCompat.getColor(this, R.color.custom_red))
            imageRight.setImageResource(R.drawable.ic_wrong)
            textRight.text = "Не правильно!"
            imageRight.visibility = View.VISIBLE
            textRight.visibility = View.VISIBLE
            //Toast.makeText(this, "Неправильно!", Toast.LENGTH_SHORT).show()
            var textWrong = findViewById<TextView>(R.id.numWrong)
            var num = textWrong.text.toString().toInt() + 1
            textWrong.text = num.toString()
        }
        lifecycleScope.launch { setNewWord() }  // Обновляем слово и варианты ответов
    }

    private fun nextWord(){
        lifecycleScope.launch { setNewWord() }
        var textWrong = findViewById<TextView>(R.id.numWrong)
        var num = textWrong.text.toString().toInt() + 1
        textWrong.text = num.toString()
    }

}

