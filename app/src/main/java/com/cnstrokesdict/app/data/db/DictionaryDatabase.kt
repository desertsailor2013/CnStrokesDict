package com.cnstrokesdict.app.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        DictMeta::class,
        DictPayload::class,
        DictMetaFts::class,
        WordEntity::class,
        WordPackageEntity::class,
        UserSettingsEntity::class,
    ],
    version = 2,
    exportSchema = false,
)
abstract class DictionaryDatabase : RoomDatabase() {
    abstract fun dictionaryDao(): DictionaryDao
    abstract fun wordDao(): WordDao

    companion object {
        @Volatile
        private var instance: DictionaryDatabase? = null

        /** 版本1 → 2：新增 words、word_packages、user_settings 表 */
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS words (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        word TEXT NOT NULL,
                        pinyin TEXT NOT NULL DEFAULT '',
                        meaning TEXT NOT NULL DEFAULT '',
                        example TEXT NOT NULL DEFAULT '',
                        word_length INTEGER NOT NULL DEFAULT 2,
                        grade INTEGER NOT NULL DEFAULT 0,
                        semester INTEGER NOT NULL DEFAULT 0,
                        unit INTEGER NOT NULL DEFAULT 0,
                        lesson INTEGER NOT NULL DEFAULT 0,
                        package_id INTEGER NOT NULL DEFAULT 0,
                        created_at INTEGER NOT NULL DEFAULT 0
                    )
                    """
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_words_package_id ON words (package_id)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_words_word ON words (word)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_words_grade_semester ON words (grade, semester)")

                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS word_packages (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        name TEXT NOT NULL,
                        description TEXT NOT NULL DEFAULT '',
                        author TEXT NOT NULL DEFAULT '',
                        grade INTEGER NOT NULL DEFAULT 0,
                        semester INTEGER NOT NULL DEFAULT 0,
                        word_count INTEGER NOT NULL DEFAULT 0,
                        qr_code_data TEXT NOT NULL DEFAULT '',
                        file_path TEXT NOT NULL DEFAULT '',
                        is_builtin INTEGER NOT NULL DEFAULT 0,
                        is_active INTEGER NOT NULL DEFAULT 1,
                        created_at INTEGER NOT NULL DEFAULT 0,
                        updated_at INTEGER NOT NULL DEFAULT 0
                    )
                    """
                )

                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS user_settings (
                        key TEXT NOT NULL,
                        value TEXT NOT NULL DEFAULT '',
                        updated_at INTEGER NOT NULL DEFAULT 0,
                        PRIMARY KEY(key)
                    )
                    """
                )

                // 创建 FTS5 索引
                db.execSQL(
                    """
                    CREATE VIRTUAL TABLE IF NOT EXISTS words_fts USING fts5(
                        word, pinyin, meaning,
                        content=words,
                        content_rowid=id
                    )
                    """
                )
            }
        }

        fun getInstance(context: Context): DictionaryDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    DictionaryDatabase::class.java,
                    "dictionary.db",
                )
                    .addMigrations(MIGRATION_1_2)
                    .build().also { instance = it }
            }
    }
}
