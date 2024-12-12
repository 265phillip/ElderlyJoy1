package com.phillipkaundeni265.ElderJoy1

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment

class ExercisesFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_exercises, container, false)

        val waterSection = view.findViewById<RelativeLayout>(R.id.water_section_id)
        waterSection.setOnClickListener { openWaterFragment() }

        val exerciseSection = view.findViewById<RelativeLayout>(R.id.exerciseSection)
        exerciseSection.setOnClickListener { openExerciseActivity() }

        val sleepSection = view.findViewById<RelativeLayout>(R.id.sleep_section_id)
        sleepSection.setOnClickListener { openSleepActivity() }

        val stressSection = view.findViewById<RelativeLayout>(R.id.stress_section_id)
        stressSection.setOnClickListener { openStressActivity() }

        return view
    }

    private fun openWaterFragment() {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, WaterFragment())
        transaction.addToBackStack(null)
        transaction.commit()

        if (activity is MainActivity) {
            (activity as MainActivity).checkPermissions()
        }
    }

    private fun openExerciseActivity() {
        val intent = Intent(activity, Exercise::class.java)
        startActivity(intent)
    }

    private fun openSleepActivity() {
        val intent = Intent(activity, Sleep::class.java)
        startActivity(intent)
    }

    private fun openStressActivity() {
        val intent = Intent(activity, Stress::class.java)
        startActivity(intent)
    }
}
