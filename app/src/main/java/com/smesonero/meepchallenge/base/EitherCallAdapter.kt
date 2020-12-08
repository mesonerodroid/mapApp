package com.smesonero.meepchallenge.base

import arrow.core.Either
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

internal class EitherCallAdapter<R>(
    private val successType: Type
) : CallAdapter<R, Call<Either<ApiError, R>>> {

    override fun adapt(call: Call<R>): Call<Either<ApiError, R>> = EitherCall(call, successType)

    override fun responseType(): Type = successType
}