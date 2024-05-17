package com.amtech.baseetcustomer.MainActivity.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amtech.baseetcustomer.R
import com.amtech.baseetcustomer.databinding.FragmentMyBookingBinding
import com.amtech.baseetcustomer.databinding.FragmentNotificationBinding


class NotificationFragment : Fragment() {
    private lateinit var  binding: FragmentNotificationBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentNotificationBinding.bind(view)
    }
}