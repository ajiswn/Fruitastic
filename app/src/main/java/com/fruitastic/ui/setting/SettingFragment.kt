package com.fruitastic.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.fruitastic.R
import com.fruitastic.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettingViewModel by viewModels { ViewModelFactory(SettingPreferences.getInstance(requireActivity().application.dataStore)) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val switchTheme = binding.switchTheme
        val switchAutoSave = binding.switchAutoSave

        viewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
                binding.tvValueTheme.text = getString(R.string.dark)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
                binding.tvValueTheme.text = getString(R.string.light)
            }
        }

        viewModel.getAutoSaveSetting().observe(viewLifecycleOwner) { isAutoSaveActive: Boolean ->
            if (isAutoSaveActive) {
                switchAutoSave.isChecked = true
                binding.tvValueSave.text = getString(R.string.active)
            } else {
                switchAutoSave.isChecked = false
                binding.tvValueSave.text = getString(R.string.inactive)
            }
        }

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            viewModel.saveThemeSetting(isChecked)
        }
        switchAutoSave.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveAutoSaveSetting(isChecked)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}