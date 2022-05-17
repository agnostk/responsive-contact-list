package br.edu.ufabc.listacontatosresponsiva.model

import android.util.Log
import br.edu.ufabc.listacontatosresponsiva.MainActivity
import com.beust.klaxon.Klaxon
import com.beust.klaxon.KlaxonException
import com.google.android.material.snackbar.Snackbar
import java.io.FileNotFoundException
import java.io.InputStream

class Repository {
    private lateinit var contacts: List<Contact>

    fun loadData(inputStream: InputStream) {
        try {
            contacts = Klaxon().parseArray(inputStream) ?: emptyList()
        } catch (e: FileNotFoundException) {
            throw Exception("Failed to open dataset file", e)
        } catch (e: KlaxonException) {
            throw java.lang.Exception("Failed to parse dataset file", e)
        }
    }

    fun getAll() = if (this::contacts.isInitialized) contacts else
        throw Exception("No data has been fetched yet")
}