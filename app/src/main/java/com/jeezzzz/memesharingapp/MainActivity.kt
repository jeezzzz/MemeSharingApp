package com.jeezzzz.memesharingapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.jeezzzz.memesharingapp.databinding.ActivityMainBinding

@SuppressLint("StaticFieldLeak")
lateinit var binding: ActivityMainBinding
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadMeme()
        binding.nextButton.setOnClickListener {
            loadMeme()
        }
        binding.shareButton.setOnClickListener {
            shareMeme()
        }

    }

    @SuppressLint("SetTextI18n")
    private fun loadMeme() {
        val url = "https://meme-api.com/gimme"
        binding.progressBar.visibility = View.VISIBLE
        binding.memeImage.visibility = View.GONE
        binding.credits.visibility=View.GONE
        val queue: RequestQueue = Volley.newRequestQueue(this)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
              val imageURL = response.getString("url")
                val text=response.getString("author")
                binding.credits.text="Credits: $text"
                Glide.with(this).load(imageURL).into(binding.memeImage)
                binding.progressBar.visibility = View.GONE
                binding.memeImage.visibility = View.VISIBLE
                binding.credits.visibility=View.VISIBLE

            },
            { error ->
              Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
            }

        )
        queue.add(jsonObjectRequest)

    }

    private fun shareMeme() {

        val url = "https://meme-api.com/gimme"
        val queue: RequestQueue = Volley.newRequestQueue(this)

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                val imageUrl = response.getString("url")
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, imageUrl)
                startActivity(Intent.createChooser(shareIntent, "Share Meme"))
            },
            { error ->
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        )
        queue.add(jsonObjectRequest)

    }

}