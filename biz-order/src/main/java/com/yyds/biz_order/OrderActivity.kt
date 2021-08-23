package com.yyds.biz_order

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.mikeyyds.common.route.MikeRoute
import com.mikeyyds.common.route.RouteFlag
import com.mikeyyds.common.ui.component.MikeBaseActivity
import com.mikeyyds.common.ui.view.loadUrl
import com.mikeyyds.library.util.MikeRes
import com.mikeyyds.library.util.MikeStatusBar
import com.yyds.biz_order.address.AddDialogFragment
import com.yyds.biz_order.address.Address
import com.yyds.biz_order.databinding.ActivityOrderBinding

@Route(path = "/order/main", extras = RouteFlag.FLAG_LOGIN)
class OrderActivity : MikeBaseActivity() {
    @JvmField
    @Autowired
    var shopName: String? = null

    @JvmField
    @Autowired
    var shopLogo: String? = null

    @JvmField
    @Autowired
    var goodsId: String? = null

    @JvmField
    @Autowired
    var goodsName: String? = null

    @JvmField
    @Autowired
    var goodsImage: String? = null

    @JvmField
    @Autowired
    var goodsPrice: String? = null

    private val REQUEST_CODE_ADDRESS_LIST = 1000;

    private lateinit var binding: ActivityOrderBinding

    private val viewModel by viewModels<OrderViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MikeStatusBar.setStatusBar(this, true, translucent = false)
        MikeRoute.inject(this)

        initView()
        updateTotalPayPrice(binding.amountView.getAmountValue())

        viewModel.queryMainAddress().observe(this, Observer {
            updateAddress(it)
        })
    }

    private fun updateAddress(address: Address?) {
        val hasMainAddress = address != null && !TextUtils.isEmpty(address.receiver)
        binding.addAddress.visibility = if (hasMainAddress) View.GONE else View.VISIBLE
        binding.mainAddress.visibility = if (!hasMainAddress) View.GONE else View.VISIBLE
        if (hasMainAddress) {
            binding.userName.text = address!!.receiver
            binding.userPhone.text = address!!.phoneNum
            binding.userAddress.text = "${address.province} ${address.city} ${address.area}"
            binding.mainAddress.setOnClickListener {
/*                val addDialogFragment = AddDialogFragment.newInstance(address)
                addDialogFragment.setSavedAddressListener(object :AddDialogFragment.OnSavedAddressListener{
                    override fun onSavedAddress(address: Address) {
                        updateAddress(address)
                    }
                })
                addDialogFragment.show(supportFragmentManager,"update_address")*/
                MikeRoute.startActivity(
                    this, destination = MikeRoute.Destination.ADDRESS_LIST,
                    requestCode = REQUEST_CODE_ADDRESS_LIST
                )
            }
        } else {
            binding.addAddress.setOnClickListener {
                val addDialogFragment = AddDialogFragment.newInstance(null)
                addDialogFragment.setSavedAddressListener(object :
                    AddDialogFragment.OnSavedAddressListener {
                    override fun onSavedAddress(address: Address) {
                        updateAddress(address)
                    }
                })
                addDialogFragment.show(supportFragmentManager, "add_address")
            }
        }
    }

    private fun initView() {
        binding.navBar.setNavListener(View.OnClickListener { onBackPressed() })
        shopLogo?.apply { binding.shopLogo.loadUrl(this) }
        binding.shopTitle.text = shopName
        goodsImage?.apply { binding.goodsImage.loadUrl(this) }
        binding.goodsTitle.text = goodsName
        binding.goodsPrice.text = goodsPrice

        binding.amountView.setAmountValueChangedListener {
            updateTotalPayPrice(it)
        }

        binding.channelWxPay.setOnClickListener(channelPayListener)
        binding.channelAliPay.setOnClickListener(channelPayListener)

        binding.orderNow.setOnClickListener {
            showToast("not support for now")
        }


    }

    private fun updateTotalPayPrice(amount: Int) {
        binding.totalPayPrice.text = String.format(
            MikeRes.getString(
                R.string.free_transport,
                PriceUtil.calculate(goodsPrice, amount)
            )
        )
    }

    private val channelPayListener = View.OnClickListener {
        val aliPayChecked = it.id == binding.channelAliPay.id
        binding.channelAliPay.isChecked = aliPayChecked
        binding.channelWxPay.isChecked = !aliPayChecked
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data!=null&&requestCode==REQUEST_CODE_ADDRESS_LIST&&resultCode== Activity.RESULT_OK){
            val address = data.getParcelableExtra<Address>("result")
            if (address!=null){
                updateAddress(address)
            }
        }
    }

}