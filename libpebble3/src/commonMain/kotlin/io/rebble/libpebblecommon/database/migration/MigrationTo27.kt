package io.rebble.libpebblecommon.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.SQLiteException
import androidx.sqlite.execSQL

object MigrationTo27 : Migration(26, 27) {
    override fun migrate(connection: SQLiteConnection) {
        // Hack alert
        // There was a conflict in version 25: microPebble migrated to a database version with systemapp added, while
        // Core version migrated to a version with syncEvents added
        // To consolidate them in version 27, we attempt to apply both of them, so, regardless of which database is updated from,
        // version 27 should contain updates from both and be in sync.

        try {
            connection.execSQL("ALTER TABLE `CalendarEntity` ADD COLUMN `syncEvents` INTEGER NOT NULL DEFAULT 1")
        } catch (e: SQLiteException) {
            if (e.message?.contains("duplicate column name") == true) {
                // Expected, do nothing
            } else {
                throw e
            }
        }

        try {
            connection.execSQL("ALTER TABLE `LockerEntryEntity` ADD COLUMN `systemApp` INTEGER NOT NULL DEFAULT 0")
        } catch (e: SQLiteException) {
            if (e.message?.contains("duplicate column name") == true) {
                // Expected, do nothing
            } else {
                throw e
            }
        }

    }
}
