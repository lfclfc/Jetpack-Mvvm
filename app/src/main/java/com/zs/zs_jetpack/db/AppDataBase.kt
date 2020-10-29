package com.zs.zs_jetpack.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.zs.zs_jetpack.play.collect.CollectAudioBean
import com.zs.zs_jetpack.play.collect.CollectAudioDao

/**
 * des
 * @author zs
 * @date 2020/10/29
 */
@Database(entities = [HistoryAudioBean::class,CollectAudioBean::class],version = 1,exportSchema = false)
abstract class AppDataBase: RoomDatabase() {
    /**
     * 获取HistoryAudioDao
     */
    abstract fun historyDao():HistoryAudioDao

    /**
     * 获取CollectAudioBean
     */
    abstract fun collectDao():CollectAudioDao


    companion object{
        @Volatile
        private var instance:AppDataBase? = null

        fun getInstance(context: Context):AppDataBase{
            return instance?: synchronized(this){
                instance?:buildDataBase(context)
                    .also {
                        instance = it
                    }
            }
        }

        private fun buildDataBase(context: Context):AppDataBase{
            return Room
                .databaseBuilder(context,AppDataBase::class.java,"jet-database")
                .addCallback(object :RoomDatabase.Callback(){
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                    }
                })
                .build()
        }
    }
}