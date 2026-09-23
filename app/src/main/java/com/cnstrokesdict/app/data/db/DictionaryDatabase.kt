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
        IdiomEntity::class,
        IdiomFts::class,
        LearningRecordEntity::class,
        LearningStatsEntity::class,
    ],
    version = 3,
    exportSchema = false,
)
abstract class DictionaryDatabase : RoomDatabase() {
    abstract fun dictionaryDao(): DictionaryDao
    abstract fun wordDao(): WordDao
    abstract fun idiomDao(): IdiomDao
    abstract fun learningDao(): LearningDao

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

        /** 版本2 → 3：新增 idioms、idioms_fts、learning_records、learning_stats 表 */
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS idioms (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        idiom TEXT NOT NULL,
                        pinyin TEXT NOT NULL DEFAULT '',
                        meaning TEXT NOT NULL DEFAULT '',
                        source TEXT NOT NULL DEFAULT '',
                        examples TEXT NOT NULL DEFAULT '',
                        synonyms TEXT NOT NULL DEFAULT '',
                        antonyms TEXT NOT NULL DEFAULT '',
                        category TEXT NOT NULL DEFAULT '',
                        difficulty INTEGER NOT NULL DEFAULT 1,
                        created_at INTEGER NOT NULL DEFAULT 0
                    )
                    """
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_idioms_idiom ON idioms (idiom)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_idioms_category ON idioms (category)")

                db.execSQL(
                    """
                    CREATE VIRTUAL TABLE IF NOT EXISTS idioms_fts USING fts5(
                        idiom, pinyin, meaning,
                        content=idioms,
                        content_rowid=id
                    )
                    """
                )

                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS learning_records (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        word TEXT NOT NULL,
                        type TEXT NOT NULL,
                        is_correct INTEGER NOT NULL DEFAULT 0,
                        time_spent INTEGER NOT NULL DEFAULT 0,
                        created_at INTEGER NOT NULL DEFAULT 0
                    )
                    """
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_learning_records_word ON learning_records (word)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_learning_records_created_at ON learning_records (created_at)")

                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS learning_stats (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        date TEXT NOT NULL,
                        total_count INTEGER NOT NULL DEFAULT 0,
                        correct_count INTEGER NOT NULL DEFAULT 0,
                        error_count INTEGER NOT NULL DEFAULT 0,
                        time_spent INTEGER NOT NULL DEFAULT 0,
                        created_at INTEGER NOT NULL DEFAULT 0
                    )
                    """
                )
                db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_learning_stats_date ON learning_stats (date)")
            }
        }

        fun getInstance(context: Context): DictionaryDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    DictionaryDatabase::class.java,
                    "dictionary.db",
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .build().also { instance = it }
            }
    }
}
