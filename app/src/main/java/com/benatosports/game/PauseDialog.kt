package com.benatosports.game

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.benatosports.game.databinding.PauseDialogBinding

class PauseDialog(val resume: ()->Unit,val restart: ()->Unit,val exit: ()->Unit): DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var adb = AlertDialog.Builder(requireContext())
        var view = PauseDialogBinding.inflate(layoutInflater,null,false)
        adb = adb.setView(view.root)
        view.resume.setOnClickListener {
            dismiss()
            resume()
        }
        view.restart.setOnClickListener {
            dismiss()
            restart()
        }
        view.exit.setOnClickListener {
            dismiss()
            exit()
        }
        return adb.create().apply {
            window!!.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }
}