package com.example.three_we_mobile.repository

import android.util.Log
import androidx.room.withTransaction
import com.example.three_we_mobile.db.RoomDB
import com.example.three_we_mobile.db.entity.AppData
import com.example.three_we_mobile.network.MyApi
import com.example.three_we_mobile.utils.Resource
import com.example.three_we_mobile.utils.networkBoundResource
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(
    private val api: MyApi,
    private val db: RoomDB
) {

    private val appDao = db.getDaoApp()

    fun getAppDataServer(
        forceRefresh: Boolean,
        onFetchSuccess: () -> Unit,
        onFetchFailed: (Throwable) -> Unit
    ): Flow<Resource<AppData>> =
        networkBoundResource(
            query = {
                appDao.getAllApp()
            },
            fetch = {
                val response = api.getWeishEndpoint()
                response
            },
            saveFetchResult = { data ->
                if (data.isSuccessful) {
                    db.withTransaction {
                        appDao.deleteAllApp()
                        appDao.savaAllApp(data.body()!!)
                    }
                }
            },
            shouldFetch = { _ ->
                forceRefresh
            },
            onFetchSuccess = onFetchSuccess,
            onFetchFailed = {
                if (it !is HttpException && it !is IOException) {
                    throw it
                }
                onFetchFailed(it)
            })

}