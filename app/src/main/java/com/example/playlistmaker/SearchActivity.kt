package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.appcompat.widget.Toolbar

class SearchActivity : AppCompatActivity() {

    private var searchQuery: String = "" // Переменная для сохранения текста

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)

        val searchEditText = findViewById<EditText>(R.id.input_search_form)
        val clearButton = findViewById<ImageView>(R.id.button_clear_search_form)
        val toolbar: Toolbar = findViewById(R.id.search_toolbar)

        // Настроим кнопку "Назад" на Toolbar
        toolbar.setNavigationOnClickListener {
            // Используем новый метод для обработки "Назад"
            onBackPressedDispatcher.onBackPressed() // вызываем новый метод для выхода
        }

        // Устанавливаем обработчик для отображения системных отступов
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.search)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Добавляем TextWatcher для управления видимостью кнопки очистки
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
                searchQuery = s.toString() // Сохраняем текст в переменную
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Обрабатываем нажатие на кнопку очистки
        clearButton.setOnClickListener {
            searchEditText.text.clear() // Очищаем текст
            searchEditText.clearFocus() // Снимаем фокус с поля
            hideKeyboard(searchEditText) // Скрываем клавиатуру
        }

        // Устанавливаем фокус на поле при его нажатии и показываем клавиатуру
        searchEditText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                showKeyboard(v)
            }
        }

        // Восстанавливаем текст из сохранённого состояния, если есть
        savedInstanceState?.getString(KEY_SEARCH_QUERY)?.let {
            searchQuery = it
            searchEditText.setText(it)
        }
    }

    // Сохраняем текст при изменении конфигурации
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_SEARCH_QUERY, searchQuery)
    }

    // Метод для скрытия клавиатуры
    private fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    // Метод для показа клавиатуры
    private fun showKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    companion object {
        private const val KEY_SEARCH_QUERY = "SEARCH_QUERY"
    }
}