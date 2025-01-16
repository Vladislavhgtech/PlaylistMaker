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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.LinearLayout
import com.google.android.material.button.MaterialButton

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private var searchQuery: String = "" // Переменная для сохранения текста
    private lateinit var trackList: ArrayList<Track>
    private lateinit var trackRecyclerView: RecyclerView
    private lateinit var trackAdapter: TrackAdapter

    private lateinit var placeholderNothingWasFound: TextView
    private lateinit var placeholderCommunicationsProblem: LinearLayout
    private lateinit var buttonRetry: MaterialButton

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com")  // Базовый URL для API iTunes
        .addConverterFactory(GsonConverterFactory.create())  // Используем конвертер Gson для преобразования данных
        .build()

    private val iTunesApi: ITunesApi by lazy { RetrofitClient.createApi() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)

        val searchEditText = findViewById<EditText>(R.id.input_search_form)
        val clearButton = findViewById<ImageView>(R.id.button_clear_search_form)
        val toolbar: Toolbar = findViewById(R.id.search_toolbar)

        trackList = arrayListOf()

        // Инициализация плейсхолдеров
        placeholderNothingWasFound = findViewById(R.id.placeholderNothingWasFound)
        placeholderCommunicationsProblem = findViewById(R.id.placeholderCommunicationsProblem)
        buttonRetry = findViewById(R.id.button_retry)


        trackRecyclerView = findViewById(R.id.track_recycler_view)
        trackAdapter = TrackAdapter(trackList)
        trackRecyclerView.layoutManager = LinearLayoutManager(this)
        trackRecyclerView.adapter = trackAdapter

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Устанавливаем обработчик для отображения системных отступов
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.search)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
                searchQuery = s.toString()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        clearButton.setOnClickListener {
            searchEditText.text.clear()  // Очищаем текст в поле
            searchEditText.clearFocus()  // Снимаем фокус с поля ввода
            hideKeyboard(searchEditText) // Скрываем клавиатуру

            // Сбрасываем результаты поиска и обновляем видимость
            resetSearchResults()
        }

        // Обработка нажатия на кнопку "Done"
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                performSearch(searchQuery)
            }
            false
        }



        // Обрабатываем нажатие на кнопку очистки
        clearButton.setOnClickListener {
            searchEditText.text.clear()
            searchEditText.clearFocus()
            hideKeyboard(searchEditText)


            clearSearchResults()
        }

        // Устанавливаем фокус на поле при его нажатии и показываем клавиатуру
        searchEditText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                showKeyboard(v)
            }
        }

        // Обработчик клика на кнопку "Обновить"
        buttonRetry.setOnClickListener {
            performSearch(searchQuery)  // Повторный запрос
        }

        // Восстанавливаем текст из сохранённого состояния, если есть
        savedInstanceState?.getString(KEY_SEARCH_QUERY)?.let {
            searchQuery = it
            searchEditText.setText(it)
        }
    }

    private fun clearSearchResults() {
        // Очистить список треков
        trackList.clear()
        trackAdapter.notifyDataSetChanged() // Обновить адаптер

        // Скрыть RecyclerView и плейсхолдеры
        trackRecyclerView.visibility = View.GONE
        placeholderNothingWasFound.visibility = View.GONE
        placeholderCommunicationsProblem.visibility = View.GONE
    }


    private fun resetSearchResults() {
        trackList.clear() // Очистить список треков
        trackAdapter.notifyDataSetChanged() // Уведомить адаптер о том, что данные изменились

        trackRecyclerView.visibility = View.GONE // Скрыть RecyclerView
        placeholderNothingWasFound.visibility = View.GONE // Скрыть плейсхолдер "Нет результатов"
        placeholderCommunicationsProblem.visibility = View.GONE // Скрыть плейсхолдер ошибки
    }

    private fun performSearch(query: String) {

        resetSearchResults()

        if (query.isNotEmpty()) {
            iTunesApi.searchTracks(query).enqueue(object : Callback<TrackResponse> {
                override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>) {
                    // Скрываем плейсхолдеры
                    placeholderNothingWasFound.visibility = View.GONE
                    placeholderCommunicationsProblem.visibility = View.GONE
                    trackRecyclerView.visibility = View.VISIBLE

                    if (response.isSuccessful && response.body() != null) {
                        val tracks = response.body()?.results ?: emptyList()

                        if (tracks.isEmpty()) {
                            // Плейсхолдер "Нет результатов"
                            placeholderNothingWasFound.visibility = View.VISIBLE
                        } else {
                            // Отображаем список результатов
                            trackList.clear()
                            trackList.addAll(tracks)
                            trackAdapter.notifyDataSetChanged()
                        }
                    } else {
                        placeholderCommunicationsProblem.visibility = View.VISIBLE
                    }
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    // Плейсхолдер "Ошибка сервера"
                    placeholderCommunicationsProblem.visibility = View.VISIBLE
                }
            })
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