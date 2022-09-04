package com.side.project.messenger.data.repo

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.side.project.messenger.utils.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface FirebaseStoreRepo {
    fun signUp(users: HashMap<String, Any>): Task<DocumentReference>
    fun signIn(account: String, password: String): Task<QuerySnapshot>
    fun updateUsers(userId: String): DocumentReference
}

class FirebaseStoreRepoImpl: FirebaseStoreRepo, KoinComponent {
    private val mFirestore: FirebaseFirestore by inject()

    override fun signUp(users: HashMap<String, Any>): Task<DocumentReference> {
        return mFirestore.collection(KEY_COLLECTION_USERS)
            .add(users)
    }

    override fun signIn(account: String, password: String): Task<QuerySnapshot> {
        return mFirestore.collection(KEY_COLLECTION_USERS)
            .whereEqualTo(if (verifyEmail(account)) KEY_USER_EMAIL else KEY_USER_PHONE, account)
            .whereEqualTo(KEY_USER_PASSWORD, password)
            .get()
    }

    override fun updateUsers(userId: String): DocumentReference {
        return mFirestore.collection(KEY_COLLECTION_USERS)
            .document(userId)
    }
}