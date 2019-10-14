package us.lidaka.counter

import android.content.Context

import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

class WidgetState(var id: Int, var label: String, var step: Int, var count: Int) : Serializable {

    fun persist(context: Context) {
        try {
            val fos = context.openFileOutput(filenameForId(id), Context.MODE_PRIVATE)
            val oos = ObjectOutputStream(fos)
            oos.writeObject(this)
        } catch (e: IOException) {
        }

    }

    companion object {
        private const val serialVersionUID: Long = 1

        fun load(context: Context, id: Int): WidgetState? {
            return try {
                val fis = context.openFileInput(filenameForId(id))
                val ois = ObjectInputStream(fis)
                ois.readObject() as WidgetState
            } catch (e: IOException) {
                null
            } catch (e: ClassNotFoundException) {
                null
            }
        }

        private fun filenameForId(id: Int): String {
            return "widget.$id"
        }
    }
}
