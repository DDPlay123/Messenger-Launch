package com.side.project.messenger.utils

// Bundle 解析
const val PARSE_TYPE = "ParseType"

enum class ActivityParseType {
    SIGN_UP, SIGN_IN, SING_OUT
}

// Data Share Preference
const val KEY_RECORD_USERS = "RecordUsers"

enum class UsersPreference {
    USERS_PREFERENCE, ACCOUNT_SHARE
}

// Account Detail
const val KEY_COLLECTION_USERS = "Users"
const val KEY_USER_ID = "ID"
const val KEY_USER_IMAGE = "Image"
const val KEY_USER_FIRST_NAME = "FirstName"
const val KEY_USER_LAST_NAME = "LastName"
const val KEY_USER_GENDER = "Gender"
const val KEY_USER_PHONE = "Phone"
const val KEY_USER_EMAIL = "Email"
const val KEY_USER_PASSWORD = "Password"