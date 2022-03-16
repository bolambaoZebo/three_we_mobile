package com.example.three_we_mobile.utils

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType) -> Boolean = { true },
    crossinline onFetchSuccess: () -> Unit = { },
    crossinline onFetchFailed: (Throwable) -> Unit = { }
) = channelFlow {
    val data = query().first()

    if (shouldFetch(data)) {
        val loading = launch {
            query().collect { send(Resource.Loading(it)) }
        }

        try {
            saveFetchResult(fetch())
            onFetchSuccess()
            loading.cancel()
            query().collect { send(Resource.Success(it)) }
        } catch (t: Throwable) {
            onFetchFailed(t)
            loading.cancel()
            query().collect { send(Resource.Error(t, it)) }
        }
    } else {
        query().collect { send(Resource.Success(it)) }
    }
}


//= flow {
//    val data = query().first()
//
//    val flow = if (shouldFetch(data)){
////        val loading = launch {
////            query().collect { send(Resource.Loading(it)) }
////        }
//        emit(Resource.Loading(data))
//        try {
//            saveFetchResult(fetch())
//            onFetchSuccess()
//            query().map { Resource.Success(it) }
//        }catch (throwable: Throwable) {
//            query().map { Resource.Error(throwable, it) }
//        }
//    }else {
//        query().map { Resource.Success(it) }
//    }
//
//    emitAll(flow)
//}