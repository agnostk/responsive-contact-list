package br.edu.ufabc.listacontatosslidingpane

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import br.edu.ufabc.listacontatosslidingpane.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    private class CustomOnBackPressedCallback(
        private val slidingPaneLayout: SlidingPaneLayout,
        private val viewModel: MainViewModel
    ) : OnBackPressedCallback(slidingPaneLayout.isSlideable && slidingPaneLayout.isOpen),
        SlidingPaneLayout.PanelSlideListener {

        init {
            slidingPaneLayout.addPanelSlideListener(this)
        }

        override fun handleOnBackPressed() {
            slidingPaneLayout.close()
            viewModel.clickedItemId.value = null
        }

        override fun onPanelSlide(panel: View, slideOffset: Float) {}

        override fun onPanelOpened(panel: View) {
            isEnabled = true
        }

        override fun onPanelClosed(panel: View) {
            isEnabled = false
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bindEvents()
    }

    private fun bindEvents() {
        try {
            viewModel.clickedItemId.observe(this) {
                it?.let {
                    supportFragmentManager.commit {
                        ContactItemFragment().apply {
                            replace(binding.contactItemFragmentContainer.id, this)
                            onBackPressedDispatcher.addCallback(
                                this@MainActivity,
                                CustomOnBackPressedCallback(binding.slidingPaneLayout, viewModel)
                            )
                        }
                    }
                    binding.slidingPaneLayout.open()
                }
            }
        } catch (e: IllegalStateException) {
            Log.e("APP", e.message, e)
            Snackbar.make(
                binding.root.rootView,
                "Failed to build app layout",
                Snackbar.LENGTH_INDEFINITE
            ).show()
        }
    }
}