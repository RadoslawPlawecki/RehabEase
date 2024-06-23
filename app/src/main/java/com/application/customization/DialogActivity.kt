package com.application.customization

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog

open class DialogActivity(private val context: Context, private val layoutResourceId: Int) {
    private var dialog: AlertDialog? = null
    fun getDialog(): View {
        val layoutInflater = LayoutInflater.from(context)
        val v = layoutInflater.inflate(layoutResourceId, null)
        val addDialog = AlertDialog.Builder(context)
        addDialog.setView(v)
        dialog = addDialog.create()
        dialog?.show()
        return v
    }

    fun closeDialog() {
        dialog?.dismiss()
        dialog = null
    }
}