package br.edu.ufabc.listacontatosresponsiva

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import br.edu.ufabc.listacontatosresponsiva.databinding.ContactListItemBinding
import br.edu.ufabc.listacontatosresponsiva.databinding.FragmentContactListBinding

class ContactListFragment(private val contacts: List<Contact>) : Fragment() {
    private lateinit var binding: FragmentContactListBinding

    companion object {
        const val itemClickedKey = "itemClickedKey"
        const val itemClickedPosition = "itemClickedPosition"
    }

    private inner class ContactAdapter(val contacts: List<Contact>) :
        RecyclerView.Adapter<ContactAdapter.ContactHolder>() {

        private inner class ContactHolder(itemBinding: ContactListItemBinding) :
            RecyclerView.ViewHolder(itemBinding.root) {
            val name = itemBinding.contactListItemFullName

            init {
                itemBinding.root.setOnClickListener {
                    setFragmentResult(
                        itemClickedKey,
                        bundleOf(itemClickedPosition to bindingAdapterPosition)
                    )
                }
            }
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ContactHolder = ContactHolder(
            ContactListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

        override fun onBindViewHolder(holder: ContactHolder, position: Int) {
            val contact = contacts[position]
            holder.name.text = contact.name
        }

        override fun getItemCount(): Int = contacts.size

        override fun onViewRecycled(holder: ContactHolder) {
            super.onViewRecycled(holder)
            Log.d("APP", "Recycled holder at position ${holder.adapterPosition}")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.recyclerviewContactList.apply {
            adapter = ContactAdapter(contacts)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }
}