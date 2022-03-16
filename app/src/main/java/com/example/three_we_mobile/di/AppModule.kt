package com.example.three_we_mobile.di

import android.app.Application
import com.example.three_we_mobile.db.RoomDB
import com.example.three_we_mobile.db.dao.AppDao
import com.example.three_we_mobile.network.MyApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofig(): Retrofit =
        Retrofit.Builder()
            .baseUrl(MyApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideMainApi(retrofit: Retrofit): MyApi =
        retrofit.create(MyApi::class.java)

    @Provides
    @Singleton
    fun getAppDB(context: Application) : RoomDB {
        return RoomDB.getAppDBInstance(context)
    }

    @Provides
    @Singleton
    fun getSpotDao(roomDB: RoomDB) : AppDao {
        return roomDB.getDaoApp()
    }
//
//    @Provides
//    @Singleton
//    fun getAdsDao(roomDB: RoomDB) : AdsDao {
//        return roomDB.getDaoAds()
//    }
//
//    @Provides
//    @Singleton
//    fun getAppDao(roomDB: RoomDB) : AppDao {
//        return roomDB.getDaoApp()
//    }

    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())

}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope