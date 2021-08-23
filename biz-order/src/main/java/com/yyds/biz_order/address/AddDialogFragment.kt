package com.yyds.biz_order.address

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.viewModels
import com.mikeyyds.library.util.MikeRes
import com.yyds.biz_order.R
import androidx.lifecycle.Observer
import com.mikeyyds.common.city.CityMgr
import com.mikeyyds.common.city.CityModel
import com.mikeyyds.library.util.showToast
import com.mikeyyds.ui.cityselector.CitySelectorDialogFragment
import com.mikeyyds.ui.cityselector.Province

import com.yyds.biz_order.databinding.DialogAddNewAddressBinding
import java.util.*

class AddDialogFragment : AppCompatDialogFragment() {
    private var address: Address? = null
    private var selectProvince: Province? = null
    private lateinit var binding: DialogAddNewAddressBinding
    private val viewModel by viewModels<AddressViewModel>()
    private var savedAddressListener: OnSavedAddressListener? = null


    companion object {
        const val KEY_ADDRESS_PARAMS = "key_address"
        fun newInstance(address: Address?): AddDialogFragment {
            var args = Bundle()
            args.putParcelable(KEY_ADDRESS_PARAMS, address)
            val fragment = AddDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        address = arguments?.getParcelable<Address>(KEY_ADDRESS_PARAMS)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val window = dialog?.window
        val root = window?.findViewById<ViewGroup>(android.R.id.content) ?: container
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )

        binding = DialogAddNewAddressBinding.inflate(inflater, root, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val closeBtn = binding.navBar.addRightTextButton(
            R.string.if_close, R.id.nav_id_close
        )
        closeBtn.textSize = 25f
        closeBtn.setOnClickListener { dismiss() }

        binding.addressPick.getEditText()
            .setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_right_arrow, 0)
        binding.addressPick.getEditText().isFocusable = false
        binding.addressPick.getEditText().isFocusableInTouchMode = false
        binding.addressPick.getEditText().setOnClickListener {
            val liveData = CityMgr.getCityData()
            liveData.removeObservers(viewLifecycleOwner)
            liveData.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    val citySelector = CitySelectorDialogFragment.newInstance(selectProvince, it)
                    citySelector.setCitySelectListener(object :
                        CitySelectorDialogFragment.onCitySelectListener {
                        override fun onCitySelect(province: Province) {
                            updateAddress(province)
                        }
                    })
                    citySelector.show(childFragmentManager, "city_selector")
                } else {
                    showToast("Null Province List")
                }
            })
        }

        binding.addressDetail.getTitleView().gravity = Gravity.TOP
        binding.addressDetail.getEditText().gravity = Gravity.TOP
        binding.addressDetail.getEditText().isSingleLine = false

        if (address != null) {
            binding.userName.getEditText().setText(address!!.receiver)
            binding.userPhone.getEditText().setText(address!!.phoneNum)
            binding.addressPick.getEditText()
                .setText(address!!.province + " " + address!!.city + " " + address!!.area)
            binding.addressDetail.getEditText().setText(address!!.detail)
        }

        binding.actionSaveAddress.setOnClickListener {
            savedAddress()
        }
    }

    private fun updateAddress(province: Province) {
        this.selectProvince = province
        binding.addressPick.getEditText()
            .setText(province.districtName.toString() + " " + province.selectCity?.districtName + " " + province.selectDistrict?.districtName)
    }

    private fun savedAddress() {
        val phone = binding.userPhone.getEditText().text.toString().trim()
        val receiver = binding.userName.getEditText().text.toString().trim()
        val detail = binding.addressDetail.getEditText().text.toString().trim()
        val cityArea = binding.addressPick.getEditText().text.toString().trim()

        if (TextUtils.isEmpty(phone)
            || TextUtils.isEmpty(receiver)
            || TextUtils.isEmpty(detail)
            || TextUtils.isEmpty(cityArea)
        ) {
            Toast.makeText(
                context,
                MikeRes.getString(R.string.address_info_too_simple),
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val province = selectProvince?.districtName ?: address?.province
        val city = selectProvince?.selectCity?.districtName ?: address?.city
        val district = selectProvince?.selectDistrict?.districtName ?: address?.area

        if (TextUtils.isEmpty(province)
            || TextUtils.isEmpty(city)
            || TextUtils.isEmpty(district)
        ) {
            Toast.makeText(
                context,
                MikeRes.getString(R.string.address_info_too_simple),
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (address == null) {
            viewModel.saveAddress(province!!, city!!, district!!, detail, receiver, phone)
                .observe(viewLifecycleOwner, observer)
        } else {
            viewModel.updateAddress(
                address!!.id,
                province!!,
                city!!,
                district!!,
                detail,
                receiver,
                phone
            )
                .observe(viewLifecycleOwner, observer)
        }
    }

    private val observer = Observer<Address?> {
        if (it != null) {
            savedAddressListener?.onSavedAddress(it)
            dismiss()
        }
    }

    fun setSavedAddressListener(listener: OnSavedAddressListener) {
        this.savedAddressListener = listener
    }

    interface OnSavedAddressListener {
        fun onSavedAddress(address: Address)
    }
}