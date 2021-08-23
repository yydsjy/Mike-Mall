package com.yyds.biz_order.address

import android.app.AlertDialog
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.mikeyyds.library.util.MikeRes
import com.mikeyyds.ui.icfont.IconFontTextView
import com.mikeyyds.ui.item.core.MikeDataItem
import com.yyds.biz_order.R

class AddressItem(
    var address: Address,
    val fm: FragmentManager,
    val removeItemCallback: (Address, AddressItem) -> Unit,
    val itemClickCallback: (Address) -> Unit,
    val viewModel: AddressViewModel
) :
    MikeDataItem<Address, RecyclerView.ViewHolder>() {

    override fun onBindData(holder: RecyclerView.ViewHolder, position: Int) {
        val context = holder.itemView.context
        holder.itemView.findViewById<TextView>(R.id.user_name).text = address.receiver
        holder.itemView.findViewById<TextView>(R.id.user_phone).text = address.phoneNum
        holder.itemView.findViewById<TextView>(R.id.user_address).text =
            address.province + " " + address.city + " " + address.area
        holder.itemView.findViewById<TextView>(R.id.edit_address).setOnClickListener {
            val dialog = AddDialogFragment.newInstance(address)
            dialog.setSavedAddressListener(object : AddDialogFragment.OnSavedAddressListener {
                override fun onSavedAddress(newAddress: Address) {
                    address = newAddress
                    refreshItem()
                }
            })
            dialog.show(fm, "edit_address")
        }

        holder.itemView.setOnClickListener {
            itemClickCallback(address)
        }


        holder.itemView.findViewById<IconFontTextView>(R.id.close).setOnClickListener {
            AlertDialog.Builder(context).setMessage(R.string.address_delete_title)
                .setNegativeButton(R.string.address_delete_cancel, null)
                .setPositiveButton(R.string.address_delete_ensure) { dialog, which ->
                    dialog.dismiss()
                    removeItemCallback(address, this)
                }.show()
        }

        val select = viewModel.checkedAddressItem == this && viewModel.checkedPostion == position

        holder.itemView.findViewById<TextView>(R.id.default_address)
            .setTextColor(MikeRes.getColor(if (select) R.color.color_DD2 else R.color.color_999))

        holder.itemView.findViewById<TextView>(R.id.default_address)
            .setText(MikeRes.getString(if (select) R.string.address_default else R.string.set_default_address))

        holder.itemView.findViewById<TextView>(R.id.default_address)
            .setCompoundDrawablesWithIntrinsicBounds(
                if (select) R.drawable.ic_checked_red else 0,
                0,
                0,
                0
            )

        holder.itemView.findViewById<TextView>(R.id.default_address).setOnClickListener {
            viewModel.checkedPostion = position
            viewModel.checkedAddressItem?.refreshItem()
            viewModel.checkedAddressItem = this
            refreshItem()
        }


    }

    override fun getItemLayoutRes(): Int {
        return R.layout.activity_address_list_item
    }
}