package com.yyds.biz_order.address

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.mikeyyds.common.route.RouteFlag
import com.mikeyyds.common.ui.component.MikeBaseActivity
import com.mikeyyds.common.ui.view.EmptyView
import com.mikeyyds.library.util.MikeStatusBar
import com.mikeyyds.ui.item.core.MikeAdapter
import com.yyds.biz_order.R
import com.yyds.biz_order.databinding.ActivityAddressListBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
@Route(path = "/address/list", extras = RouteFlag.FLAG_LOGIN)
class AddressListActivity : MikeBaseActivity() {

    private lateinit var binding: ActivityAddressListBinding

    private val viewModel by viewModels<AddressViewModel>()

    @Inject
    lateinit var emptyView: EmptyView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MikeStatusBar.setStatusBar(this, true, translucent = false)
        binding = ActivityAddressListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        viewModel.queryAddressList().observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                bindData(it)
            } else {
                showEmptyView(true)
            }
        })
    }

    private fun bindData(list: List<Address>) {
        val items = arrayListOf<AddressItem>()
        val mikeAdapter = binding.recyclerView.adapter as MikeAdapter
        for (address in list) {
            val addressItem = newAddressItem(address)
            addressItem.setAdapter(mikeAdapter)

            items.add(addressItem)
        }

        mikeAdapter.clearItems()
        mikeAdapter.addItems(items, true)
    }

    private fun newAddressItem(address: Address): AddressItem {
        return AddressItem(address, supportFragmentManager, itemClickCallback = { address ->
            val intent = Intent()
            intent.putExtra("result", address)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }, removeItemCallback = { address, addressItem ->
            viewModel.deleteAddress(address.id)
                .observe(this@AddressListActivity, Observer { success ->
                    if (success) {
                        addressItem.removeItem()

                    }
                })
        }, viewModel = viewModel)
    }


    private fun initView() {
        binding.navBar.setNavListener(View.OnClickListener { onBackPressed() })
        binding.navBar.addRightTextButton(R.string.nav_add_address, R.id.nav_id_add_address)
            .setOnClickListener {
                showEditDiaglog()
            }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = MikeAdapter(this)
        binding.recyclerView.adapter?.registerAdapterDataObserver(adapterDataObserver)

    }

    private fun showEditDiaglog() {
        val addDialogFragment = AddDialogFragment.newInstance(null)
        addDialogFragment.setSavedAddressListener(object :
            AddDialogFragment.OnSavedAddressListener {
            override fun onSavedAddress(address: Address) {
                val mikeAdapter = binding.recyclerView.adapter as MikeAdapter?
                mikeAdapter?.addItemAt(0, newAddressItem(address), true)

            }
        })
        addDialogFragment.show(supportFragmentManager, "add_address")
    }

    private val adapterDataObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            showEmptyView(false)
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            binding.recyclerView.post {
                if (binding.recyclerView.adapter!!.itemCount <= 0) {
                    showEmptyView(true)
                }
            }
        }
    }

    private fun showEmptyView(showEmptyView: Boolean) {
        binding.recyclerView.isVisible = !showEmptyView
        emptyView.isVisible = showEmptyView
        if (emptyView.parent == null && showEmptyView) {
            binding.rootLayout.addView(emptyView)
        }
    }

    override fun onDestroy() {
        binding.recyclerView?.adapter?.unregisterAdapterDataObserver(adapterDataObserver)
        super.onDestroy()
    }
}