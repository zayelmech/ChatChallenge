package com.imecatro.chatchallenge

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.imecatro.chatchallenge.databinding.FragmentMessageListBinding

/**
 * A fragment representing a list of Items.
 */
class MessageFragment : Fragment() {


    private var _binding: FragmentMessageListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var userName: String? = null


    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            userName = it.getString(USER)
        }
    }

    lateinit var recyclerViewAdapter: MessageRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMessageListBinding.inflate(inflater, container, false)


        // Set the adapter
        recyclerViewAdapter = MessageRecyclerViewAdapter(mutableListOf())
        with(binding.list) {
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerViewAdapter
        }



        auth.signInAnonymously()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = task.result.user
                    //val token = task.result.credential
                    //Log.d("TOKEN",token.toString())
                    Toast.makeText(context, "AUTENTICADO EXITOSAMENTE $user", Toast.LENGTH_LONG)
                        .show()

//                    val database = Firebase.database
//                    databaseRef = database.getReference("Chat")
//                    databaseRef.get().let { task ->
//                        task.result.let { dataSnapshot ->
//                            dataSnapshot.getValue<HashMap<String, FastChat>>()?.let { database ->
//                                database.values.let {
//                                    recyclerViewAdapter.updateList(it.sortedBy { fastChat ->
//                                        fastChat.timeMillis
//                                    }.toList())
//                                }
//                            }
//
//                        }
//                    }

                } else {
                    Toast.makeText(context, "NO SE PUDO AUTENTICAR", Toast.LENGTH_LONG)
                        .show()

                }
            }

        binding.sendButton.setOnClickListener {

            val text: String = binding.textInput.text.toString()

            basicReadWrite(text)
        }

        return binding.root
    }


    private fun basicReadWrite(message: String) {
        // [START listen_document]
        // Write a message to the database
        val database = Firebase.database
        //var user = website.replace("https://", "")
//        user = user.replace(".", "-")

        val refMillis = System.currentTimeMillis()
        val myRef = database.getReference("Chat/${refMillis}")

        myRef.setValue(FastChat(this.userName ?: "default", message, refMillis))
        // myRef.removeValue()

        myRef.get()

        databaseRef = database.getReference("Chat")

        databaseRef.addValueEventListener(FirebaseListener())

    }

    data class FastChat(
        val user: String = "",
        val message: String = "",
        val timeMillis: Long = 0L
    )


    inner class FirebaseListener : ValueEventListener {

        override fun onDataChange(snapshot: DataSnapshot) {

            Log.d("CLASS::${javaClass.simpleName}", "Event Occurs on database ref")

            val value = snapshot.getValue<HashMap<String, FastChat>>()
            //val value = snapshot.getValue<List<FastChat>>()


            value!!.values.let {

                recyclerViewAdapter.updateList(it.sortedBy { fastChat ->
                    fastChat.timeMillis
                }.toList())
//
//                it.takeIf { token ->
//                    token.user_id.equals(user)
//                }
            }

//            if (!wooToken?.consumer_key.equals("null") && wooToken != null) {
//
//                val consumerKey: String = wooToken.consumer_key ?: "null"
//                val consumerSecret = wooToken.consumer_secret ?: "null"

//                sharedPreferences.saveSecret(consumerKey, consumerSecret)
//                sharedPreferences.saveWebUrl(url)
//                openMainActivity()


//            }

        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("CLASS::${javaClass.simpleName}", "Failed to read value.", error.toException())

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        // TODO: Customize parameter argument names
        const val USER = "userId"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(user: String) =
            MessageFragment().apply {
                arguments = Bundle().apply {
                    putString(USER, user)
                }
            }
    }
}