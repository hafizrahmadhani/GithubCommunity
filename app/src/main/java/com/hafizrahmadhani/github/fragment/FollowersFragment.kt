package com.hafizrahmadhani.github.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hafizrahmadhani.github.datamodel.DataModelRv
import com.hafizrahmadhani.github.R
import com.hafizrahmadhani.github.viewmodel.ViewModelFollowers
import com.hafizrahmadhani.github.adapter.FollowersAdapter
import kotlinx.android.synthetic.main.fragment_followers.*

class FollowersFragment : Fragment() {

    companion object {
        const val EXTRA_NAME = "extra_name"
    }

    private lateinit var viewModelFollowers: ViewModelFollowers
    private lateinit var rvGitUser: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_followers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val username = arguments?.getString(EXTRA_NAME)
        rvGitUser = view.findViewById(R.id.rv_gitUser)
        rvGitUser.layoutManager = LinearLayoutManager(activity)

        callProgressBar(true)
        viewModelFollowers = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(ViewModelFollowers::class.java)

        username?.let { viewModelFollowers.callFollowers(it) }
        viewModelFollowers.takeError().observe(viewLifecycleOwner, {
            when (it) {
                true -> callErrorMsg()
                else -> viewModelFollowers.callFollowers(username as String)
            }
        })

        viewModelFollowers.takeFollowers().observe(viewLifecycleOwner, {
            callListFollowers(it)
            callProgressBar(false)
        })
    }

    private fun callErrorMsg() {
        viewModelFollowers.takeMsgError().observe(viewLifecycleOwner, {
            Toast.makeText(view?.context, it, Toast.LENGTH_LONG).show()
        })
    }

    private fun callListFollowers(listFollowers: ArrayList<DataModelRv>) {
        val adapter = FollowersAdapter(listFollowers)
        rv_gitUser.adapter = adapter
    }

    private fun callProgressBar(status: Boolean) {
        if (status) {
            progress_bar.visibility = View.VISIBLE
        } else progress_bar.visibility = View.GONE
    }
}