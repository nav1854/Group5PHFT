package com.example.group5phft

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "UserDatabase.db"
        const val DATABASE_VERSION = 4

        // User table and columns
        const val TABLE_USERS = "users"
        const val COLUMN_ID = "id"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_FIRST_NAME = "first_name"
        const val COLUMN_LAST_NAME = "last_name"
        const val COLUMN_DOB = "dob"
        const val COLUMN_SUBSCRIPTION = "subscription_plan"

        // Workout plans table and columns
        const val TABLE_WORKOUT_PLANS = "workout_plans"
        const val COLUMN_PLAN_ID = "plan_id"
        const val COLUMN_PLAN_TITLE = "plan_title"
        const val COLUMN_PLAN_DURATION = "plan_duration"
        const val COLUMN_PLAN_SETS = "plan_sets"
        const val COLUMN_PLAN_DESCRIPTION = "plan_description"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Create users table
        val createUsersTable = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_EMAIL TEXT UNIQUE,
                $COLUMN_PASSWORD TEXT,
                $COLUMN_FIRST_NAME TEXT,
                $COLUMN_LAST_NAME TEXT,
                $COLUMN_DOB TEXT,
                $COLUMN_SUBSCRIPTION TEXT DEFAULT NULL
            )
        """.trimIndent()

        // Create workout plans table
        val createWorkoutPlansTable = """
            CREATE TABLE $TABLE_WORKOUT_PLANS (
                $COLUMN_PLAN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_PLAN_TITLE TEXT NOT NULL,
                $COLUMN_PLAN_DURATION TEXT,
                $COLUMN_PLAN_SETS INTEGER,
                $COLUMN_PLAN_DESCRIPTION TEXT
            )
        """.trimIndent()

        db.execSQL(createUsersTable)
        db.execSQL(createWorkoutPlansTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_WORKOUT_PLANS")
        onCreate(db)
    }

    // Insert user into the users table
    fun insertUser(email: String, password: String, firstName: String, lastName: String, dob: String?): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_EMAIL, email)
            put(COLUMN_PASSWORD, password)
            put(COLUMN_FIRST_NAME, firstName)
            put(COLUMN_LAST_NAME, lastName)
            put(COLUMN_DOB, dob)
        }
        return db.insert(TABLE_USERS, null, values) != -1L
    }

    // Validate user credentials
    fun validateUser(email: String, password: String): Boolean {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?"
        val cursor = db.rawQuery(query, arrayOf(email, password))
        val isValid = cursor.count > 0
        cursor.close()
        return isValid
    }

    // Update user subscription
    fun updateSubscription(email: String, subscriptionPlan: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_SUBSCRIPTION, subscriptionPlan)
        }
        return db.update(TABLE_USERS, values, "$COLUMN_EMAIL = ?", arrayOf(email)) > 0
    }

    // Get user subscription
    //change
    fun getUserSubscription(userEmail: String?): String? {
        if (userEmail.isNullOrEmpty()) return null
        val db = readableDatabase
        val query = "SELECT $COLUMN_SUBSCRIPTION FROM $TABLE_USERS WHERE $COLUMN_EMAIL = ?"
        val cursor = db.rawQuery(query, arrayOf(userEmail))

        var subscription: String? = null
        if (cursor.moveToFirst()) {
            subscription = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUBSCRIPTION))
        }
        cursor.close()
        return subscription
    }

    // Insert workout plan into the workout plans table
    //look into
    fun insertWorkoutPlan(title: String, duration: String?, sets: Int?, description: String?): Boolean {
        if (title.isBlank()) return false // Title is mandatory
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_PLAN_TITLE, title)
            put(COLUMN_PLAN_DURATION, duration ?: "")
            put(COLUMN_PLAN_SETS, sets ?: 0)
            put(COLUMN_PLAN_DESCRIPTION, description ?: "")
        }
        return db.insert(TABLE_WORKOUT_PLANS, null, values) != -1L
    }

    // Retrieve all workout plans
    //look into
    fun getAllWorkoutPlans(): List<Map<String, Any>> {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_WORKOUT_PLANS"
        val cursor = db.rawQuery(query, null)

        val plans = mutableListOf<Map<String, Any>>()
        while (cursor.moveToNext()) {
            val plan = mapOf(
                COLUMN_PLAN_ID to cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PLAN_ID)),
                COLUMN_PLAN_TITLE to cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PLAN_TITLE)),
                COLUMN_PLAN_DURATION to cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PLAN_DURATION)),
                COLUMN_PLAN_SETS to cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PLAN_SETS)),
                COLUMN_PLAN_DESCRIPTION to cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PLAN_DESCRIPTION))
            )
            plans.add(plan)
        }
        cursor.close()
        return plans
    }

    // Delete a workout plan by ID
    fun deleteWorkoutPlan(planId: Int): Boolean {
        val db = writableDatabase
        val selection = "$COLUMN_PLAN_ID = ?"
        val selectionArgs = arrayOf(planId.toString())

        val rowsDeleted = db.delete(TABLE_WORKOUT_PLANS, selection, selectionArgs)
        db.close()

        return rowsDeleted > 0  // Return true if at least one row was deleted
    }
}
