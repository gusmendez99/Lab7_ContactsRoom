package com.gustavomendez.lab3contacts.Providers

import android.content.*
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import android.text.TextUtils

class ContactsProvider : ContentProvider() {

    /**
     * Database specific constant declarations
     */

    companion object {
        private const val PROVIDER_NAME = "com.gustavomendez.ContactsProvider"
        internal const val URL = "content://$PROVIDER_NAME/contacts"
        internal val CONTENT_URI = Uri.parse(URL)

        internal const val _ID = "_id"
        internal const val NAME = "name"
        internal const val EMAIL = "email"
        internal const val PHONE = "phone"
        internal const val IMAGE_PATH = "image_path"

        private val CONTACTS_PROJECTION_MAP: HashMap<String, String>? = null

        private const val CONTACTS = 1
        internal const val CONTACTS_1 = 2

        internal val uriMatcher: UriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            uriMatcher.addURI(PROVIDER_NAME, "contacts", CONTACTS)
            uriMatcher.addURI(PROVIDER_NAME, "contacts/#", CONTACTS_1)
        }

        internal const val DATABASE_NAME = "ContactBook"
        internal const val CONTACTS_TABLE_NAME = "contacts"
        internal const val DATABASE_VERSION = 1
        /**
         * Table with a image path field, can be null by default
         */
        internal const val CREATE_DB_TABLE = " CREATE TABLE " + CONTACTS_TABLE_NAME +
                " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " name TEXT NOT NULL, " +
                " email TEXT NOT NULL, " +
                " phone TEXT NOT NULL," +
                " image_path TEXT NULL);"
    }

    private var db: SQLiteDatabase? = null

    /**
     * Helper class that actually creates and manages
     * the provider's underlying data repository.
     */

    private class DatabaseHelper internal constructor(context: Context) :
        SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(CREATE_DB_TABLE)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS $CONTACTS_TABLE_NAME")
            onCreate(db)
        }
    }

    override fun onCreate(): Boolean {
        val context = context
        val dbHelper = DatabaseHelper(context)

        /**
         * Create a write able database which will trigger its
         * creation if it doesn't already exist.
         */

        db = dbHelper.writableDatabase
        return db != null
    }

    /**
     * Add new contact to SQLite
     */
    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        /**
         * Add a new student record
         */
        val rowID = db!!.insert(CONTACTS_TABLE_NAME, "", values)

        /**
         * If record is added successfully
         */
        if (rowID > 0) {
            val _uri = ContentUris.withAppendedId(CONTENT_URI, rowID)
            context!!.contentResolver.notifyChange(_uri, null)
            return _uri
        }

        throw SQLException("Failed to add a record into $uri")
    }

    /**
     * Get all contacts, or get a contact by _id
     */
    override fun query(
        uri: Uri, projection: Array<String>?,
        selection: String?, selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
//        @Suppress("NAME_SHADOWING") var sortOrder = sortOrder
        val qb = SQLiteQueryBuilder()
        qb.tables = CONTACTS_TABLE_NAME

        when (uriMatcher.match(uri)) {
            CONTACTS -> qb.setProjectionMap(CONTACTS_PROJECTION_MAP)

            CONTACTS_1 -> qb.appendWhere(_ID + "=" + uri.pathSegments[1])
        }

        val c = qb.query(
            db, projection, selection,
            selectionArgs, null, null, sortOrder ?: NAME
        )
        /**
         * register to watch a content URI for changes
         */
        c.setNotificationUri(context!!.contentResolver, uri)
        return c
    }

    /**
     * For delete a contact by _id, return zero if there's no contact with the id*
     */
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        var count = 0
        count = when (uriMatcher.match(uri)) {
            CONTACTS -> db!!.delete(CONTACTS_TABLE_NAME, selection, selectionArgs)

            CONTACTS_1 -> {
                val id = uri.pathSegments[1]
                db!!.delete(
                    CONTACTS_TABLE_NAME, _ID + " = " + id +
                            if (!TextUtils.isEmpty(selection)) " AND ($selection)" else "", selectionArgs
                )
            }
            else -> throw IllegalArgumentException("Unknown URI $uri")
        }

        context!!.contentResolver.notifyChange(uri, null)
        return count
    }

    /**
     * For update a contact by _id
     */
    override fun update(
        uri: Uri, values: ContentValues?,
        selection: String?, selectionArgs: Array<String>?
    ): Int {
        var count = 0
        when (uriMatcher.match(uri)) {
            CONTACTS -> count = db!!.update(CONTACTS_TABLE_NAME, values, selection, selectionArgs)

            CONTACTS_1 -> count = db!!.update(
                CONTACTS_TABLE_NAME, values,
                _ID + " = " + uri.pathSegments[1] +
                        if (!TextUtils.isEmpty(selection)) " AND ($selection)" else "", selectionArgs
            )
            else -> throw IllegalArgumentException("Unknown URI $uri")
        }

        context!!.contentResolver.notifyChange(uri, null)
        return count
    }

    override fun getType(uri: Uri): String? {
        when (uriMatcher.match(uri)) {
            /**
             * Get all student records
             */
            CONTACTS -> return "vnd.android.cursor.dir/vnd.example.contacts"
            /**
             * Get a particular contact
             */
            CONTACTS_1 -> return "vnd.android.cursor.item/vnd.example.contacts"
            else -> throw IllegalArgumentException("Unsupported URI: $uri")
        }
    }


}