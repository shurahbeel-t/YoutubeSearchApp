package com.observer.youtubesearchapp.view

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.observer.youtubesearchapp.R
import com.observer.youtubesearchapp.databinding.ActivityMainBinding
import com.observer.youtubesearchapp.viewmodel.ApiCallState
import com.observer.youtubesearchapp.viewmodel.SearchViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var displayMetrics: DisplayMetrics
    private lateinit var searchFragment: SearchResultsFragment
    private val searchViewModel: SearchViewModel by viewModels()

    private var animationDuration: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        animationDuration = resources.getInteger(R.integer.animationDuration).toLong()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                searchFragment = SearchResultsFragment()
                setReorderingAllowed(true)
                add(R.id.search_fragment_cv, searchFragment)
            }
        }

        // inflating the binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setStateListener()
        setSearchButtonListener()
        // I AM SO PRO >:)
    }

    private fun handleAnimation() {
        lifecycleScope.launch(Dispatchers.Main) {
            updateConstraintsToParent(true)
            val bias = calculateCurrentBias()
            setVerticalBias(bias)

            animateUpSearchEt(bias)
            animateTheMargin(binding.etSearch, animationDuration)
            animateTheBackground(binding.etSearch, animationDuration)

            launch {
                delay(150)
                animateInSearchFragment()
            }
            launch {
                val duration = animationDuration - 250
                delay(duration)
                handleBackButton(duration)
            }

            animateOutTitle()
            animateOutSearchButton()
            delay(animationDuration)
            setVisibilities()
        }
    }

    private fun updateConstraintsToParent(set: Boolean) {
        val constraintSet = getMainConstraintSet()
        constraintSet.clear(binding.etSearch.id, ConstraintSet.TOP)
        if (set) {
            constraintSet.connect(binding.etSearch.id, ConstraintSet.TOP, binding.main.id, ConstraintSet.TOP)
        } else {
            constraintSet.connect(binding.etSearch.id, ConstraintSet.TOP, binding.tvTitle.id, ConstraintSet.BOTTOM)
        }
        constraintSet.applyTo(binding.main)
    }

    private fun handleBackButton(duration: Long) {
        binding.ibBack.apply {
            alpha = 0f
            rotation = -90f
            visibility = View.VISIBLE
            animate()
                .setDuration(duration)
                .alpha(1f)
                .rotation(0f)
        }
    }

    private fun setVerticalBias(bias: Float) {
        val constraintSet = getMainConstraintSet()
        constraintSet.setVerticalBias(binding.etSearch.id, bias)
        constraintSet.applyTo(binding.main)
    }

    private fun animateUpSearchEt(startBias: Float) {
        val animator: ValueAnimator = ValueAnimator.ofFloat(startBias, 0.0f)
        animator.duration = animationDuration
        animator.addUpdateListener { animation ->
            val animatedBias = animation.animatedValue as Float
            setVerticalBias(animatedBias)
        }
        animator.start()
    }

    private fun animateTheMargin(editText: EditText, duration: Long) {
        val startValue = (editText.layoutParams as ViewGroup.MarginLayoutParams).marginEnd
        val endValue = 0
        val valueAnimator: ValueAnimator = ValueAnimator.ofInt(startValue, endValue)
        valueAnimator.duration = duration
        valueAnimator.addUpdateListener { animation ->
            val animatedValue = animation.animatedValue as Int
            editText.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                marginEnd = animatedValue
            }
        }
        valueAnimator.start()
    }

    private fun animateTheBackground(editText: EditText, duration: Long) {
        val bgAlphaAnimator: ObjectAnimator = ObjectAnimator.ofInt(editText.background, "alpha", 255, 0)
        bgAlphaAnimator.duration = duration
        bgAlphaAnimator.start()
    }

    private fun animateOutTitle() {
        binding.tvTitle.animate().setDuration(animationDuration).alpha(0f)
    }

    private fun animateOutSearchButton() {
        binding.btnSearch.animate().setDuration(animationDuration).alpha(0f)
    }

    private fun setVisibilities() {
        binding.apply {
            tvTitle.visibility = View.GONE
            btnSearch.visibility = View.GONE
        }
    }

    private fun animateInSearchFragment() {
        displayMetrics = resources.displayMetrics
        binding.searchFragmentCv.apply {
            alpha = 0f
            translationY = displayMetrics.heightPixels * 1.0f
            visibility = View.VISIBLE
            animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(animationDuration)
        }
    }

    private fun setStateListener() {
        lifecycleScope.launch(Dispatchers.Default) {
            searchViewModel.apiCallState.collect { state ->
                when (state) {
                    is ApiCallState.CallException -> {
                        Log.e("custom", "Request exception: ${state.e.message}", state.e)
                    }

                    is ApiCallState.Failed -> {
                        val errorBody = state.responseBody?.string()
                        Log.e("custom", "Api call Failed:\n${errorBody}")
                    }

                    ApiCallState.Pending -> Log.v("custom", "Api call Pending")
                    is ApiCallState.Success -> Log.v("custom", "Api call Success")
                }
            }
        }
    }

    private fun performSearch(query: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            searchViewModel.searchYoutube(applicationContext, query)
        }
    }

    private fun setSearchButtonListener() {
        val sharedListener = View.OnClickListener {
            onEnterText(null)
        }
        binding.apply {
            btnSearch.setOnClickListener(sharedListener)
            etSearch.setOnEditorActionListener { textView, actionId, keyEvent ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    onEnterText(textView)
                    true
                } else {
                    false
                }
            }
        }
    }

    private fun hideOnScreenKeyboard(v: View?) {
        if (v != null) {
            val inputMethodManager: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }

    private fun onEnterText(v: View?) {
        binding.etSearch.let { box ->
            if (box.text?.isNotBlank() == true) {
                hideOnScreenKeyboard(v)
                performSearch(getSearchQuery())
                handleAnimation()
            } else {
                Toast.makeText(this@MainActivity, "Hehe! You're funny. :D\nI can't really search for nothing.\nNow can I?", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun calculateCurrentBias(): Float {
        binding.apply {
            val parentHeight: Int = (main as View).height.toInt()
            val titleBottom: Int = (tvTitle as View).bottom.toInt()
            val availableHeight = parentHeight - titleBottom
            // default bottom inset of edit text. which is 7dp, converted to pixels
            val bottomInset = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 7f, resources.displayMetrics).toInt()
            var currentPosWrtParent: Int = (etSearch as View).bottom
            currentPosWrtParent = currentPosWrtParent - bottomInset
            val biasWrtParent: Float = (currentPosWrtParent.toDouble() / parentHeight.toDouble()).toFloat()
            Log.v(
                "custom", "parentHeight: $parentHeight, titleBottom: $titleBottom, availableHeight: $availableHeight\n" +
                        "currentPosWrtParent: $currentPosWrtParent, biasParent: $biasWrtParent"
            )
            return biasWrtParent
        }
    }

    private fun getMainConstraintSet(): ConstraintSet {
        val constraintSet = ConstraintSet()
        constraintSet.clone(binding.main)
        return constraintSet
    }

    private fun getSearchQuery(): String = binding.etSearch.text.toString()
}