package com.example.sunnyweather.ui.place

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sunnyweather.databinding.FragmentPlaceBinding

/**
@date:2022/3/21
@time:5:22 下午
@author:zhaops
@desc:
 */
class PlaceFragment : Fragment() {
    private var _binding: FragmentPlaceBinding? = null
    private val binding get() = _binding!!
    private val viewModel by lazy { ViewModelProvider(this).get(PlaceViewModel::class.java) }
    private lateinit var adapter: PlaceAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event.targetState == Lifecycle.State.CREATED) {
                    val layoutManager = LinearLayoutManager(activity)
                    binding.rl.layoutManager = layoutManager
                    adapter = PlaceAdapter(this@PlaceFragment, viewModel.placeList)
                    binding.rl.adapter = adapter
                    binding.etSearch.addTextChangedListener { editable ->
                        val content = editable.toString()
                        if (content.isNotEmpty()) {
                            viewModel.searchPlaces(content)
                        } else {
                            binding.rl.visibility = View.GONE
                            binding.ivBg.visibility = View.VISIBLE
                            viewModel.placeList.clear()
                            adapter.notifyDataSetChanged()
                        }
                    }
                    viewModel.placeLiveData.observe(this@PlaceFragment, Observer { result ->
                        val places = result.getOrNull()
                        if (places != null) {
                            binding.rl.visibility = View.VISIBLE
                            binding.ivBg.visibility = View.GONE
                            viewModel.placeList.clear()
                            viewModel.placeList.addAll(places)
                            adapter.notifyDataSetChanged()
                        } else {
                            Toast.makeText(activity, "未查询到地点", Toast.LENGTH_SHORT).show()
                            result.exceptionOrNull()?.printStackTrace()
                        }
                    })
                    source.lifecycle.removeObserver(this)
                }
            }
        })
    }
}