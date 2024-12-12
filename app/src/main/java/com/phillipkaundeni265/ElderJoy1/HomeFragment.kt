package com.phillipkaundeni265.ElderJoy1

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.phillipkaundeni265.ElderJoy1.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // Declare the articleAdapter and articleList
    private lateinit var articleAdapter: ArticleAdapter
    private val articleList = ArrayList<Article>() // Initialize the article list

    private val SLEEP_PREFERENCES = "sleep_preferences"
    private val SLEEP_RECORDS = "sleep_records"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView for articles
        binding.ArticlesRecycleViewId.layoutManager = LinearLayoutManager(context)
        articleAdapter = ArticleAdapter(requireContext(), articleList) // Initialize the adapter
        binding.ArticlesRecycleViewId.adapter = articleAdapter

        // Load articles into the adapter
        loadArticles()

        // Load sleep summary from SharedPreferences
        loadSleepSummary()
    }

    private fun loadSleepSummary() {
        // Get sleep records from SharedPreferences
        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences(SLEEP_PREFERENCES, Context.MODE_PRIVATE)
        val sleepRecords = sharedPreferences.getString(SLEEP_RECORDS, "No sleep records available")

        // Display the sleep records in the "TotalMedsId" TextView
        binding.TotalMedsId.text = sleepRecords
    }

    private fun loadArticles() {
        // For demonstration, add static articles.
        articleList.add(Article("https://www.everydayhealth.com/fitness/all-articles/", "Fitness"))
        articleList.add(Article("https://www.everydayhealth.com/fitness/all-articles/", "Fitness"))
        articleList.add(Article("https://www.everydayhealth.com/fitness/all-articles/", "Fitness"))

        // Notify adapter about data changes
        articleAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
